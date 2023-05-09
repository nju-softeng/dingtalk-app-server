package com.softeng.dingtalk.service;

import com.softeng.dingtalk.component.convertor.PatentConvertor;
import com.softeng.dingtalk.dao.repository.*;
import com.softeng.dingtalk.dto.req.PatentReq;
import com.softeng.dingtalk.dto.resp.PatentResp;
import com.softeng.dingtalk.po_entity.*;
import com.softeng.dingtalk.utils.StreamUtils;
import com.softeng.dingtalk.vo.PatentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class PatentService {
    @Autowired
    PatentRepository patentRepository;
    @Autowired
    PatentLevelRepository patentLevelRepository;
    @Autowired
    PatentDetailRepository patentDetailRepository;
    @Autowired
    AcRecordRepository acRecordRepository;
    @Autowired
    FileService fileService;
    @Autowired
    UserRepository userRepository;
    @Resource
    PatentConvertor patentConvertor;

    @Value("${patent.rank1Rate}")
    private double rank1Rate;

    @Value("${patent.DefaultRate}")
    private double defaultRate;

    //将增加专利和修改合并
    public void addPatent(MultipartFile file, PatentVO patentVO){
        Patent patent =null;
        if(patentVO.getId()==null){
            patent =new Patent(patentVO.getName(),patentVO.getYear(),patentVO.getType(),
                    patentVO.getVersion(),patentVO.getObligee(),patentVO.getFilePath());
        }else{
            patent =patentRepository.findById(patentVO.getId()).get();
            patent.update(patentVO.getName(),patentVO.getYear(),patentVO.getType(),
                    patentVO.getVersion(),patentVO.getObligee(),patentVO.getFilePath());
        }
        patent.setApplicant(userRepository.findById(patentVO.getApplicantId()).get());
        if(file!=null){
            if(patent.getReviewFileId()!=null){
                fileService.deleteFileByPath(patent.getReviewFileName(), patent.getReviewFileId());
            }
            String fileId=fileService.addFileByPath(file, patent.getFilePath()+"/Review");
            patent.setReviewFileName(file.getOriginalFilename());
            patent.setReviewFileId(fileId);
        }
        patentRepository.save(patent);
        //经过内审状态后，不可以再修改发明者名单
        if(patent.getState()!=0)return;
        if(patentVO.getInventorsIdList()!=null){
            List<PatentDetail> patentDetailList =new LinkedList<>();
            int num=1;
            for(Integer id:patentVO.getInventorsIdList()){
                PatentDetail patentDetail =new PatentDetail(num, patent,userRepository.findById(id).get());
                patentDetailList.add(patentDetail);
                num++;
            }
            patentDetailRepository.saveBatch(patentDetailList);
        }
    }

    public void deletePatent(int id){
        patentRepository.deleteById(id);
    }

    public Map<String, Object> getPatentList(int page, int size){
        Pageable pageable = PageRequest.of(page-1,size, Sort.by("id").descending());
        Page<Patent> patents=patentRepository.findAll(pageable);
        List<Patent> infoList=patents.toList();
        return Map.of("list",infoList,"total",patents.getTotalElements());
    }

    public Patent getPatentDetail(int id){
        return patentRepository.findById(id).get();
    }

    private double calculateRatioOfAc(int rank) {
        switch (rank) {
            case 1:
                return rank1Rate;
            default:
                return defaultRate;
        }
    }

    private double calculateAC(int signal,int rank,double sum){
        return signal*calculateRatioOfAc(rank)*sum;
    }

    //内审决定
    public void decideAudit(int id,boolean isPass,int uid){
        Patent patent =patentRepository.findById(id).get();
        if(isPass){
            double sum=patentLevelRepository.getValue("patent");
            patent.getPatentDetailList().forEach(patentDetail -> {
                double ac=calculateAC(1, patentDetail.getNum(),sum);
                if(ac!=0){
                    AcRecord acRecord =new AcRecord(patentDetail.getUser(),userRepository.findById(uid).get(),ac,"专利内审通过", AcRecord.Patent, LocalDateTime.now());
                    acRecordRepository.save(acRecord);
//                    patentDetail.getAcRecordList().add(acRecord);
                }

            });
            patent.setState(2);
        } else {
            patent.setState(1);
        }
        patentRepository.save(patent);
        patentDetailRepository.saveAll(patent.getPatentDetailList());
    }
    //授权决定
    public void decideAuthorization(int id,boolean isPass,int uid){
        Patent patent =patentRepository.findById(id).get();
        int signal;
        String reason;
        if(isPass){
            signal=1;
            reason="专利授权成功";
            patent.setState(3);
        } else {
            signal=-1;
            reason="专利授权被驳回";
            patent.setState(4);
        }
        double sum=patentLevelRepository.getValue("patent");
        patent.getPatentDetailList().forEach(patentDetail -> {
            double ac=calculateAC(signal, patentDetail.getNum(),sum);
            if(ac!=0){
                AcRecord acRecord =new AcRecord(patentDetail.getUser(),userRepository.findById(uid).get(),ac,reason, AcRecord.Patent, LocalDateTime.now());
                acRecordRepository.save(acRecord);
//                patentDetail.getAcRecordList().add(acRecord);
            }

        });
        patentDetailRepository.saveAll(patent.getPatentDetailList());
        patentRepository.save(patent);
    }

    public void addPatentFile(MultipartFile file, int id, String type){
        Patent patent =patentRepository.findById(id).get();
        PatentFileInfo patentFileInfo=getPatentFileInfo(patent,type);
        if(patentFileInfo.name!=null){
            fileService.deleteFileByPath(patentFileInfo.name,patentFileInfo.id);
        }
        String fileId=fileService.addFileByPath(file, patent.getFilePath()+"/"+getFileTypeFolderName(type));
        setPatentFileAttribute(patent,type,file.getOriginalFilename(),fileId);
        patentRepository.save(patent);
    }

    public void deletePatentFile(int id, String type){
        Patent patent =patentRepository.findById(id).get();
        PatentFileInfo patentFileInfo=getPatentFileInfo(patent,type);
        fileService.deleteFileByPath(patentFileInfo.name,patentFileInfo.id);
        setPatentFileAttribute(patent,type,null,null);
    }

    public void downloadPatentFile(int id, String type, HttpServletResponse response){
        Patent patent =patentRepository.findById(id).get();
        PatentFileInfo patentFileInfo=getPatentFileInfo(patent,type);
        try{
            fileService.downloadFile(patentFileInfo.name,patentFileInfo.id,response);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private void setPatentFileAttribute(Patent patent, String type, String fileName, String fileId){
        switch(type){
            case "reviewFile":
                patent.setReviewFileName(fileName);
                patent.setReviewFileId(fileId);
                break;
            case "submissionFile":
                patent.setSubmissionFileName(fileName);
                patent.setSubmissionFileId(fileId);
                break;
            case "commentFile":
                patent.setCommentFileName(fileName);
                patent.setCommentFileId(fileId);
                break;
            case "handlingFile":
                patent.setHandlingFileName(fileName);
                patent.setHandlingFileId(fileName);
                break;
            case"authorizationFile":
                patent.setAuthorizationFileName(fileName);
                patent.setAuthorizationFileId(fileId);
                break;
            default:
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "未找到对应文件类型！");
        }
    }

    private PatentFileInfo getPatentFileInfo(Patent patent, String type){
        PatentFileInfo patentFileInfo=new PatentFileInfo();
        switch(type){
            case "reviewFile":
                patentFileInfo.name= patent.getReviewFileName();
                patentFileInfo.id= patent.getReviewFileId();
                break;
            case "submissionFile":
                patentFileInfo.name= patent.getSubmissionFileName();
                patentFileInfo.id= patent.getSubmissionFileId();
                break;
            case "commentFile":
                patentFileInfo.name= patent.getCommentFileName();
                patentFileInfo.id= patent.getCommentFileId();
                break;
            case "handlingFile":
                patentFileInfo.name= patent.getHandlingFileName();
                patentFileInfo.id= patent.getHandlingFileId();
                break;
            case"authorizationFile":
                patentFileInfo.name= patent.getAuthorizationFileName();
                patentFileInfo.id= patent.getAuthorizationFileId();
                break;
            default:
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "未找到对应文件类型！");
        }
        return patentFileInfo;
    }

    private String getFileTypeFolderName(String fileType){
        String res=fileType.substring(0,1).toUpperCase()+fileType.substring(1,fileType.length()-4);
        return res;
    }

    static class PatentFileInfo{
        String name;
        String id;
        PatentFileInfo(String name,String id){
            this.name=name;
            this.id=id;
        }
        PatentFileInfo(){}
    }

    public Map<String, Object> queryPatentList(int page, int size, PatentReq patentReq) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());
        Specification<Patent> patentSpecification = ((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(patentReq.getInventorsIdList().size() == 1) {
//                查询特定发明人的专利
                int userId = patentReq.getInventorsIdList().get(0);

                Subquery<PatentDetail> subQuery = criteriaQuery.subquery(PatentDetail.class);
                Root<PatentDetail> subRoot = subQuery.from(PatentDetail.class);
//                subQuery.where(criteriaBuilder.equal(subRoot.get("user").get("id"), userId));
//                subQuery.select(subRoot.get("id"));
//
//                predicates.add(criteriaBuilder.exists(subQuery));

                subQuery.select(subRoot.get("patent").get("id"));
                subQuery.where(criteriaBuilder.equal(subRoot.get("user").get("id"), userId));

                predicates.add(root.get("id").in(subQuery));
            }
            if(!"".equals(patentReq.getYear())) {
                predicates.add(criteriaBuilder.equal(root.get("year"), patentReq.getYear()));
            }
            if(patentReq.getState() != -2) {
                predicates.add(criteriaBuilder.equal(root.get("state"), patentReq.getState()));
            }
            if(patentReq.getApplicantId() != 0) {
                predicates.add(criteriaBuilder.equal(root.get("applicant").get("id"), patentReq.getApplicantId()));
            }
            Predicate[] arr = new Predicate[predicates.size()];
            return criteriaBuilder.and(predicates.toArray(arr));
        });

        Page<Patent> patentPage = patentRepository.findAll(patentSpecification, pageable);
        List<PatentResp> patentRespList = StreamUtils.map(patentPage.toList(), patent ->
            patentConvertor.entity_PO2Resp(patent)
        );
        return Map.of("list", patentRespList, "total", patentPage.getTotalElements());
    }
}

package com.softeng.dingtalk.service;

import com.softeng.dingtalk.dao.repository.*;
import com.softeng.dingtalk.po.*;
import com.softeng.dingtalk.vo.PatentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
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

    @Value("${patent.rank1Rate}")
    private double rank1Rate;

    @Value("${patent.DefaultRate}")
    private double defaultRate;

    //将增加专利和修改合并
    public void addPatent(MultipartFile file, PatentVO patentVO){
        PatentPo patentPo =null;
        if(patentVO.getId()==null){
            patentPo =new PatentPo(patentVO.getName(),patentVO.getYear(),patentVO.getType(),
                    patentVO.getVersion(),patentVO.getObligee(),patentVO.getFilePath());
        }else{
            patentPo =patentRepository.findById(patentVO.getId()).get();
            patentPo.update(patentVO.getName(),patentVO.getYear(),patentVO.getType(),
                    patentVO.getVersion(),patentVO.getObligee(),patentVO.getFilePath());
        }
        patentPo.setApplicant(userRepository.findById(patentVO.getApplicantId()).get());
        if(file!=null){
            if(patentPo.getReviewFileId()!=null){
                fileService.deleteFileByPath(patentPo.getReviewFileName(), patentPo.getReviewFileId());
            }
            String fileId=fileService.addFileByPath(file, patentPo.getFilePath()+"/Review");
            patentPo.setReviewFileName(file.getOriginalFilename());
            patentPo.setReviewFileId(fileId);
        }
        patentRepository.save(patentPo);
        //经过内审状态后，不可以再修改发明者名单
        if(patentPo.getState()!=0)return;
        if(patentVO.getInventorsIdList()!=null){
            List<PatentDetailPo> patentDetailPoList =new LinkedList<>();
            int num=1;
            for(Integer id:patentVO.getInventorsIdList()){
                PatentDetailPo patentDetailPo =new PatentDetailPo(num, patentPo,userRepository.findById(id).get());
                patentDetailPoList.add(patentDetailPo);
                num++;
            }
            patentDetailRepository.saveBatch(patentDetailPoList);
        }
    }

    public void deletePatent(int id){
        patentRepository.deleteById(id);
    }

    public Map<String, Object> getPatentList(int page, int size){
        Pageable pageable = PageRequest.of(page-1,size, Sort.by("id").descending());
        Page<PatentPo> patents=patentRepository.findAll(pageable);
        List<PatentPo> infoList=patents.toList();
        return Map.of("list",infoList,"total",patents.getTotalElements());
    }

    public PatentPo getPatentDetail(int id){
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
        PatentPo patentPo =patentRepository.findById(id).get();
        if(isPass){
            double sum=patentLevelRepository.getValue("patent");
            patentPo.getPatentDetailList().forEach(patentDetailPo -> {
                double ac=calculateAC(1, patentDetailPo.getNum(),sum);
                if(ac!=0){
                    AcRecordPo acRecordPO =new AcRecordPo(patentDetailPo.getUser(),userRepository.findById(uid).get(),ac,"专利内审通过", AcRecordPo.Patent, LocalDateTime.now());
                    acRecordRepository.save(acRecordPO);
                    patentDetailPo.getAcRecordList().add(acRecordPO);
                }

            });
            patentPo.setState(2);
        } else {
            patentPo.setState(1);
        }
        patentRepository.save(patentPo);
        patentDetailRepository.saveAll(patentPo.getPatentDetailList());
    }
    //授权决定
    public void decideAuthorization(int id,boolean isPass,int uid){
        PatentPo patentPo =patentRepository.findById(id).get();
        int signal;
        String reason;
        if(isPass){
            signal=1;
            reason="专利授权成功";
            patentPo.setState(3);
        } else {
            signal=-1;
            reason="专利授权被驳回";
            patentPo.setState(4);
        }
        double sum=patentLevelRepository.getValue("patent");
        patentPo.getPatentDetailList().forEach(patentDetailPo -> {
            double ac=calculateAC(signal, patentDetailPo.getNum(),sum);
            if(ac!=0){
                AcRecordPo acRecordPO =new AcRecordPo(patentDetailPo.getUser(),userRepository.findById(uid).get(),ac,reason, AcRecordPo.Patent, LocalDateTime.now());
                acRecordRepository.save(acRecordPO);
                patentDetailPo.getAcRecordList().add(acRecordPO);
            }

        });
        patentDetailRepository.saveAll(patentPo.getPatentDetailList());
        patentRepository.save(patentPo);
    }

    public void addPatentFile(MultipartFile file, int id, String type){
        PatentPo patentPo =patentRepository.findById(id).get();
        PatentFileInfo patentFileInfo=getPatentFileInfo(patentPo,type);
        if(patentFileInfo.name!=null){
            fileService.deleteFileByPath(patentFileInfo.name,patentFileInfo.id);
        }
        String fileId=fileService.addFileByPath(file, patentPo.getFilePath()+"/"+getFileTypeFolderName(type));
        setPatentFileAttribute(patentPo,type,file.getOriginalFilename(),fileId);
        patentRepository.save(patentPo);
    }

    public void deletePatentFile(int id, String type){
        PatentPo patentPo =patentRepository.findById(id).get();
        PatentFileInfo patentFileInfo=getPatentFileInfo(patentPo,type);
        fileService.deleteFileByPath(patentFileInfo.name,patentFileInfo.id);
        setPatentFileAttribute(patentPo,type,null,null);
    }

    public void downloadPatentFile(int id, String type, HttpServletResponse response){
        PatentPo patentPo =patentRepository.findById(id).get();
        PatentFileInfo patentFileInfo=getPatentFileInfo(patentPo,type);
        try{
            fileService.downloadFile(patentFileInfo.name,patentFileInfo.id,response);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private void setPatentFileAttribute(PatentPo patentPo, String type, String fileName, String fileId){
        switch(type){
            case "reviewFile":
                patentPo.setReviewFileName(fileName);
                patentPo.setReviewFileId(fileId);
                break;
            case "submissionFile":
                patentPo.setSubmissionFileName(fileName);
                patentPo.setSubmissionFileId(fileId);
                break;
            case "commentFile":
                patentPo.setCommentFileName(fileName);
                patentPo.setCommentFileId(fileId);
                break;
            case "handlingFile":
                patentPo.setHandlingFileName(fileName);
                patentPo.setHandlingFileId(fileName);
                break;
            case"authorizationFile":
                patentPo.setAuthorizationFileName(fileName);
                patentPo.setAuthorizationFileId(fileId);
                break;
            default:
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "未找到对应文件类型！");
        }
    }

    private PatentFileInfo getPatentFileInfo(PatentPo patentPo, String type){
        PatentFileInfo patentFileInfo=new PatentFileInfo();
        switch(type){
            case "reviewFile":
                patentFileInfo.name= patentPo.getReviewFileName();
                patentFileInfo.id= patentPo.getReviewFileId();
                break;
            case "submissionFile":
                patentFileInfo.name= patentPo.getSubmissionFileName();
                patentFileInfo.id= patentPo.getSubmissionFileId();
                break;
            case "commentFile":
                patentFileInfo.name= patentPo.getCommentFileName();
                patentFileInfo.id= patentPo.getCommentFileId();
                break;
            case "handlingFile":
                patentFileInfo.name= patentPo.getHandlingFileName();
                patentFileInfo.id= patentPo.getHandlingFileId();
                break;
            case"authorizationFile":
                patentFileInfo.name= patentPo.getAuthorizationFileName();
                patentFileInfo.id= patentPo.getAuthorizationFileId();
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
}

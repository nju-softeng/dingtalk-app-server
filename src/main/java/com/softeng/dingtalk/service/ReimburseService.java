package com.softeng.dingtalk.service;

import com.softeng.dingtalk.entity.Reimbursement;
import com.softeng.dingtalk.entity.ReimbursementFile;
import com.softeng.dingtalk.repository.ReimbursementFileRepository;
import com.softeng.dingtalk.repository.ReimbursementRepository;
import com.softeng.dingtalk.vo.ReimbursementVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class ReimburseService {
    @Autowired
    ReimbursementRepository reimbursementRepository;
    @Autowired
    ReimbursementFileRepository reimbursementFileRepository;
    @Autowired
    FileService fileService;
    public void addReimbursement(ReimbursementVO reimbursementVO){
        Reimbursement reimbursement=new Reimbursement(reimbursementVO.getName(),reimbursementVO.getType(),reimbursementVO.getPath());
        reimbursementRepository.save(reimbursement);
    }

    public void updateReimbursement(ReimbursementVO reimbursementVO){
        Reimbursement reimbursement=reimbursementRepository.findById(reimbursementVO.getId()).get();
        reimbursement.update(reimbursementVO.getName(),reimbursementVO.getType(),reimbursement.getPath());
        reimbursementRepository.save(reimbursement);
    }

    public void setState(int id,int state){
        Reimbursement reimbursement=reimbursementRepository.findById(id).get();
        reimbursement.setState(state);
        reimbursementRepository.save(reimbursement);
    }

    public void addReimbursementFile(int id, MultipartFile file, String description){
        Reimbursement reimbursement=reimbursementRepository.findById(id).get();
        String fileId=fileService.addFileByPath(file,reimbursement.getPath());
        ReimbursementFile reimbursementFile=new ReimbursementFile(description,file.getOriginalFilename(),fileId,reimbursement);
        reimbursementFileRepository.save(reimbursementFile);
    }

    public void deleteReimbursementFile(int id){
        ReimbursementFile reimbursementFile=reimbursementFileRepository.findById(id).get();
        reimbursementFileRepository.delete(reimbursementFile);
    }

    public Map<String,Object> getReimbursementList(int page, int size){
        Pageable pageable = PageRequest.of(page-1,size, Sort.by("id").descending());
        Page<Reimbursement> reimbursements=reimbursementRepository.findAll(pageable);
        List<Reimbursement> reimbursementList=reimbursements.toList();
        return Map.of("list",reimbursementList,"total",reimbursements.getTotalElements());
    }

    public Reimbursement getReimbursementDetail(int id){
        return reimbursementRepository.findById(id).get();
    }

    public void deleteReimbursement(int id){
        Reimbursement reimbursement=reimbursementRepository.findById(id).get();
        if(reimbursement.getReimbursementFileList()!=null){
            for(ReimbursementFile reimbursementFile:reimbursement.getReimbursementFileList()){
                fileService.deleteFileByPath(reimbursementFile.getFileName(),reimbursement.getPath());
            }
        }
        reimbursementRepository.delete(reimbursement);
    }
}

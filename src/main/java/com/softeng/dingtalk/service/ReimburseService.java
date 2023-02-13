package com.softeng.dingtalk.service;

import com.softeng.dingtalk.po.ReimbursementPo;
import com.softeng.dingtalk.po.ReimbursementFilePo;
import com.softeng.dingtalk.dao.repository.ReimbursementFileRepository;
import com.softeng.dingtalk.dao.repository.ReimbursementRepository;
import com.softeng.dingtalk.dao.repository.UserRepository;
import com.softeng.dingtalk.vo.ReimbursementVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import javax.servlet.http.HttpServletResponse;
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
    UserRepository userRepository;
    @Autowired
    FileService fileService;
    public void addReimbursement(ReimbursementVO reimbursementVO,int id){
        ReimbursementPo reimbursementPo =new ReimbursementPo(reimbursementVO.getName(),reimbursementVO.getType(),reimbursementVO.getPath());
        reimbursementPo.setUser(userRepository.findById(id).get());
        reimbursementRepository.save(reimbursementPo);
    }

    public void updateReimbursement(ReimbursementVO reimbursementVO,int id){
        ReimbursementPo reimbursementPo =reimbursementRepository.findById(reimbursementVO.getId()).get();
        reimbursementPo.setUser(userRepository.findById(id).get());
        reimbursementPo.update(reimbursementVO.getName(),reimbursementVO.getType(), reimbursementPo.getPath());
        reimbursementRepository.save(reimbursementPo);
    }

    public void setState(int id,int state){
        ReimbursementPo reimbursementPo =reimbursementRepository.findById(id).get();
        reimbursementPo.setState(state);
        reimbursementRepository.save(reimbursementPo);
    }

    public void addReimbursementFile(int id, MultipartFile file, String description){
        ReimbursementPo reimbursementPo =reimbursementRepository.findById(id).get();
        String fileId=fileService.addFileByPath(file, reimbursementPo.getPath());
        ReimbursementFilePo reimbursementFilePo =new ReimbursementFilePo(description,file.getOriginalFilename(),fileId, reimbursementPo);
        reimbursementFileRepository.save(reimbursementFilePo);
    }

    public void deleteReimbursementFile(int id){
        ReimbursementFilePo reimbursementFilePo =reimbursementFileRepository.findById(id).get();
        reimbursementFileRepository.delete(reimbursementFilePo);
    }

    public Map<String,Object> getReimbursementList(int page, int size){
        Pageable pageable = PageRequest.of(page-1,size, Sort.by("id").descending());
        Page<ReimbursementPo> reimbursements=reimbursementRepository.findAll(pageable);
        List<ReimbursementPo> reimbursementPoList =reimbursements.toList();
        return Map.of("list", reimbursementPoList,"total",reimbursements.getTotalElements());
    }

    public ReimbursementPo getReimbursementDetail(int id){
        return reimbursementRepository.findById(id).get();
    }

    public void deleteReimbursement(int id){
        ReimbursementPo reimbursementPo =reimbursementRepository.findById(id).get();
        if(reimbursementPo.getReimbursementFilePoList()!=null){
            for(ReimbursementFilePo reimbursementFilePo : reimbursementPo.getReimbursementFilePoList()){
                fileService.deleteFileByPath(reimbursementFilePo.getFileName(), reimbursementPo.getPath());
            }
        }
        reimbursementRepository.delete(reimbursementPo);
    }

    public void downloadReimbursementFile(@PathVariable int id, HttpServletResponse response){
        ReimbursementFilePo reimbursementFilePo =reimbursementFileRepository.findById(id).get();
        try {
            fileService.downloadFile(reimbursementFilePo.getFileName(), reimbursementFilePo.getFileId(),response);
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
        }

    }
}

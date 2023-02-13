package com.softeng.dingtalk.service;

import com.softeng.dingtalk.po.DissertationPo;
import com.softeng.dingtalk.dao.repository.DissertationPropertyRepository;
import com.softeng.dingtalk.dao.repository.UserRepository;
import com.softeng.dingtalk.vo.DissertationVO;
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
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class DissertationPropertyService {
    @Autowired
    DissertationPropertyRepository dissertationPropertyRepository;
    @Autowired
    FileService fileService;
    @Autowired
    UserRepository userRepository;

    public void addDissertation(MultipartFile file, DissertationVO dissertationVO){
        String fileId=fileService.addFileByPath(file,dissertationVO.getFilePath()+"/PreRejoin");
        DissertationPo dissertationPo =new DissertationPo(dissertationVO.getState(),dissertationVO.getGraduateYear(),dissertationVO.getFilePath());
        dissertationPo.setUser(userRepository.findById(dissertationVO.getUserId()).get());
        dissertationPo.setPreRejoinFileName(file.getOriginalFilename());
        dissertationPo.setPreRejoinFileId(fileId);
        dissertationPropertyRepository.save(dissertationPo);
    }

    public void updateDissertation(DissertationVO dissertationVO){
        DissertationPo dissertationPoRec =dissertationPropertyRepository.findById(dissertationVO.getId()).get();
        dissertationPoRec.update(dissertationVO.getState(),dissertationVO.getGraduateYear());
        dissertationPropertyRepository.save(dissertationPoRec);
    }

    public Map<String, Object> getDissertation(int page, int size){
        Pageable pageable = PageRequest.of(page-1,size, Sort.by("id").descending());
        Page<DissertationPo> dissertations=dissertationPropertyRepository.findAll(pageable);
        List<DissertationPo> dissertationPoList =dissertations.stream().collect(Collectors.toList());
        return Map.of("list", dissertationPoList,"total",dissertations.getTotalElements());
    }

    public DissertationPo getDissertationDetail(@PathVariable int uid){
        DissertationPo dissertationPoRec =dissertationPropertyRepository.findByUserId(uid);
        return dissertationPoRec;
    }

    public void deleteDissertation(int id){
        DissertationPo dissertationPo =dissertationPropertyRepository.findById(id).get();
        if(dissertationPo.getPreRejoinFileId()!=null)fileService.deleteFileByPath(dissertationPo.getPreRejoinFileName(), dissertationPo.getPreRejoinFileId());
        if(dissertationPo.getReviewFileId()!=null)fileService.deleteFileByPath(dissertationPo.getReviewFileName(), dissertationPo.getReviewFileId());
        if(dissertationPo.getRejoinFileId()!=null)fileService.deleteFileByPath(dissertationPo.getRejoinFileName(), dissertationPo.getRejoinFileId());
        if(dissertationPo.getFinalFileId()!=null)fileService.deleteFileByPath(dissertationPo.getFinalFileName(), dissertationPo.getFinalFileId());
        dissertationPropertyRepository.deleteById(id);
    }

    public void addDissertationFile(MultipartFile file, String type, int id){
        DissertationPo dissertationPo =dissertationPropertyRepository.findById(id).get();
        String fileId=this.fileService.addFileByPath(file, dissertationPo.getFilePath()+"/"+getFileTypeFolderName(type));
        String fileName=file.getOriginalFilename();
        switch (type){
            case "preRejoinFile":
                dissertationPo.setPreRejoinFileId(fileId);
                dissertationPo.setPreRejoinFileName(fileName);
                break;
            case "reviewFile":
                dissertationPo.setReviewFileId(fileId);
                dissertationPo.setReviewFileName(fileName);
                break;
            case "rejoinFile":
                dissertationPo.setRejoinFileId(fileId);
                dissertationPo.setRejoinFileName(fileName);
                break;
            case "finalFile":
                dissertationPo.setFinalFileId(fileId);
                dissertationPo.setFinalFileName(fileName);
                break;
        }
        dissertationPo.setState(getDissertationState(dissertationPo));
        dissertationPropertyRepository.save(dissertationPo);
    }

    private int getDissertationState(DissertationPo dissertationPo){
        int state=3;
        if(dissertationPo.getFinalFileId()!=null) return state;
        state--;
        if(dissertationPo.getRejoinFileId()!=null) return state;
        state--;
        if(dissertationPo.getReviewFileId()!=null) return state;
        return 0;
    }
    public void deleteDissertationFile(int id,String type){
        DissertationPo dissertationPo =dissertationPropertyRepository.findById(id).get();
        String fileId=null;
        String fileName=null;
        switch (type){
            case "preRejoinFile":
                fileId= dissertationPo.getPreRejoinFileId();
                fileName= dissertationPo.getPreRejoinFileName();
                dissertationPo.setPreRejoinFileName(null);
                dissertationPo.setPreRejoinFileId(null);
                dissertationPo.setState(0);
                break;
            case "reviewFile":
                fileId= dissertationPo.getReviewFileId();
                fileName= dissertationPo.getReviewFileName();
                dissertationPo.setReviewFileName(null);
                dissertationPo.setReviewFileId(null);
                dissertationPo.setState(0);
                break;
            case "rejoinFile":
                fileId= dissertationPo.getRejoinFileId();
                fileName= dissertationPo.getRejoinFileName();
                dissertationPo.setRejoinFileName(null);
                dissertationPo.setRejoinFileId(null);
                dissertationPo.setState(1);
                break;
            case "finalFile":
                fileId= dissertationPo.getFinalFileId();
                fileName= dissertationPo.getFinalFileName();
                dissertationPo.setFinalFileName(null);
                dissertationPo.setFinalFileId(null);
                dissertationPo.setState(2);
                break;
        }
        fileService.deleteFileByPath(fileName,fileId);
        dissertationPropertyRepository.save(dissertationPo);
    }

    public void downLoadDissertationFile(int id,String type,HttpServletResponse response)  {
        DissertationFileInfo dissertationFileInfo=this.getDissertationFileInfo(id,type);
        String fileName=dissertationFileInfo.name;
        String fileId=dissertationFileInfo.id;
        try{
            fileService.downloadFile(fileName,fileId,response);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    private DissertationFileInfo getDissertationFileInfo(int id,String type){
        DissertationPo dissertationPo =dissertationPropertyRepository.findById(id).get();
        String fileName=null;
        String fileId=null;
        switch (type){
            case "preRejoinFile":
                fileId= dissertationPo.getPreRejoinFileId();
                fileName= dissertationPo.getPreRejoinFileName();
                break;
            case "reviewFile":
                fileId= dissertationPo.getReviewFileId();
                fileName= dissertationPo.getReviewFileName();
                break;
            case "rejoinFile":
                fileId= dissertationPo.getRejoinFileId();
                fileName= dissertationPo.getRejoinFileName();
                break;
            case "finalFile":
                fileId= dissertationPo.getFinalFileId();
                fileName= dissertationPo.getFinalFileName();
                break;
        }
        return new DissertationFileInfo(fileName, fileId);
    }


    static class DissertationFileInfo{
        public String name;
        public String id;
        DissertationFileInfo(String name,String id){
            this.name=name;
            this.id=id;
        }
    }

    private String getFileTypeFolderName(String fileType){
        String res=fileType.substring(0,1).toUpperCase()+fileType.substring(1,fileType.length()-4);
        return res;
    }
}

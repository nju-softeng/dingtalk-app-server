package com.softeng.dingtalk.service;

import com.softeng.dingtalk.entity.Patent;
import com.softeng.dingtalk.entity.ProcessProperty;
import com.softeng.dingtalk.entity.User;
import com.softeng.dingtalk.repository.PatentRepository;
import com.softeng.dingtalk.repository.UserRepository;
import com.softeng.dingtalk.vo.PatentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    FileService fileService;
    @Autowired
    UserRepository userRepository;

    //将增加专利和修改合并
    public void addPatent(MultipartFile file, PatentVO patentVO){
        Patent patent=null;
        if(patentVO.getId()!=null){
            patent=new Patent(patentVO.getName(),patentVO.getVersion(),patentVO.getObligee(),patentVO.getFilePath());
        }else{
            patent=patentRepository.findById(patentVO.getId()).get();
        }

        patent.setApplicant(userRepository.findById(patentVO.getApplicantId()).get());
        if(patentVO.getInventorsIdList()!=null){
            List<User> userList=new LinkedList<>();
            for(Integer id:patentVO.getInventorsIdList()){
                userList.add(userRepository.findById(id).get());
            }
            patent.setInventors(userList);
        }
        if(file!=null){
            if(patent.getPatentFileId()!=null){
                fileService.deleteFileByPath(patent.getPatentFileName(),patent.getPatentFileId());
            }
            String fileId=fileService.addFileByPath(file,patent.getFilePath()+"/Patent");
            patent.setPatentFileName(file.getOriginalFilename());
            patent.setPatentFileId(fileId);
        }
        patentRepository.save(patent);
    }

    public Map<String, Object> getPatentList(int page, int size){
        Pageable pageable = PageRequest.of(page-1,size, Sort.by("id").descending());
        Page<Patent> patents=patentRepository.findAll(pageable);
        List<Patent> infoList=patents.toList();
        return Map.of("list",infoList,"total",patents.getTotalElements());
    }
    public Patent getPatentDetail(int id){
        Patent patent=patentRepository.findById(id).get();
        return patent;
    }
    //内审决定
    public void decideAudit(){

    }
    //授权决定
    public void decideAuthorization(){

    }
}

package com.softeng.dingtalk.service;

import com.softeng.dingtalk.entity.Dissertation;
import com.softeng.dingtalk.entity.EventProperty;
import com.softeng.dingtalk.repository.DissertationPropertyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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

    public void addDissertation(MultipartFile file, Dissertation dissertation){

    }

    public void updateDissertation(Dissertation dissertation){
        Dissertation dissertationRec=dissertationPropertyRepository.findById(dissertation.getId()).get();
        dissertationRec.update(dissertation.getState(),dissertation.getGraduateYear());
        dissertationPropertyRepository.save(dissertationRec);
    }

    public Map<String, Object> getDissertation(int page, int size){
        Pageable pageable = PageRequest.of(page-1,size, Sort.by("id").descending());
        Page<Dissertation> dissertations=dissertationPropertyRepository.findAll(pageable);
        List<Dissertation> dissertationList=dissertations.stream().collect(Collectors.toList());
        return Map.of("list",dissertationList,"total",dissertations.getTotalElements());
    }

    public void deleteDissertation(int id){
        Dissertation dissertation=dissertationPropertyRepository.findById(id).get();
        if(dissertation.getPreRejoinFileId()!=null)fileService.deleteFileByPath(dissertation.getPreRejoinFileName(),dissertation.getPreRejoinFileId());
        if(dissertation.getReviewFileId()!=null)fileService.deleteFileByPath(dissertation.getReviewFileName(),dissertation.getReviewFileId());
        if(dissertation.getRejoinFileId()!=null)fileService.deleteFileByPath(dissertation.getRejoinFileName(),dissertation.getRejoinFileId());
        if(dissertation.getFinalFileId()!=null)fileService.deleteFileByPath(dissertation.getFinalFileName(),dissertation.getFinalFileId());
        dissertationPropertyRepository.deleteById(id);
    }
}

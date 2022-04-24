package com.softeng.dingtalk.service;
import com.softeng.dingtalk.entity.EventProperty;
import com.softeng.dingtalk.entity.ProjectProperty;
import com.softeng.dingtalk.repository.ProjectPropertyRepository;
import com.softeng.dingtalk.vo.ProjectPropertyVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class ProjectPropertyService {
    @Autowired
    ProjectPropertyRepository projectPropertyRepository;
    public void addProjectProperty(ProjectPropertyVO projectPropertyVO){
        ProjectProperty projectProperty=new ProjectProperty(projectPropertyVO.getName(),projectPropertyVO.getPath());
        projectPropertyRepository.save(projectProperty);
    }
    public void updateProjectProperty(ProjectPropertyVO projectPropertyVO){
        ProjectProperty projectProperty=projectPropertyRepository.findById(projectPropertyVO.getId()).get();
        projectProperty.update(projectPropertyVO.getName(),projectPropertyVO.getPath());
        projectPropertyRepository.save(projectProperty);
    }
    public Map<String, Object> getProjectPropertyList(int page, int size){
        Pageable pageable = PageRequest.of(page-1,size, Sort.by("id").descending());
        Page<ProjectProperty> projectProperties=projectPropertyRepository.findAll(pageable);
        List<ProjectPropertyVO> projectPropertyList=projectProperties.stream().map(projectProperty -> new ProjectPropertyVO(projectProperty.getId(),
                projectProperty.getName(),projectProperty.getPath())).collect(Collectors.toList());
        return Map.of("list",projectPropertyList,"total",projectProperties.getTotalElements());
    }
}

package com.softeng.dingtalk.service;
import com.softeng.dingtalk.entity.EventProperty;
import com.softeng.dingtalk.entity.ProjectProperty;
import com.softeng.dingtalk.entity.ProjectPropertyFile;
import com.softeng.dingtalk.repository.ProjectPropertyFileRepository;
import com.softeng.dingtalk.repository.ProjectPropertyRepository;
import com.softeng.dingtalk.vo.ProjectPropertyFileVO;
import com.softeng.dingtalk.vo.ProjectPropertyVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class ProjectPropertyService {
    @Autowired
    ProjectPropertyRepository projectPropertyRepository;
    @Autowired
    ProjectPropertyFileRepository projectPropertyFileRepository;
    @Autowired
    FileService fileService;

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

    public void addProjectPropertyVersion(MultipartFile codeFile, MultipartFile reportFile,
                                          String name, int id){
        ProjectProperty projectProperty=projectPropertyRepository.findById(id).get();
        String codeFileId=fileService.addFileByPath(codeFile,projectProperty.getPath()+"/"+name+"/Code");
        String reportFileId=fileService.addFileByPath(reportFile,projectProperty.getPath()+"/"+name+"/Report");
        ProjectPropertyFile projectPropertyFile=new ProjectPropertyFile(null,name,projectProperty,codeFile.getOriginalFilename(),
                codeFileId,reportFile.getOriginalFilename(),reportFileId);
        projectPropertyFileRepository.save(projectPropertyFile);
    }

    public ProjectProperty getProjectPropertyDetail(int id){
        return projectPropertyRepository.findById(id).get();
    }
    public void addProjectPropertyFile(MultipartFile file,int id,String type){
        ProjectPropertyFile projectPropertyFile=projectPropertyFileRepository.findById(id).get();
        String fileId=fileService.addFileByPath(file,projectPropertyFile.getProjectProperty().getPath()+"/"+
                projectPropertyFile.getVersion()+"/"+getFileTypeFolderName(type));
        switch (type){
            case "codeFile":
                if(projectPropertyFile.getCodeFileId()!=null){
                    fileService.deleteFileByPath(projectPropertyFile.getCodeFileName(),projectPropertyFile.getCodeFileId());
                }
                projectPropertyFile.setCodeFileId(fileId);
                projectPropertyFile.setCodeFileName(file.getOriginalFilename());
                break;
            case"reportFile":
                if(projectPropertyFile.getReportFileId()!=null){
                    fileService.deleteFileByPath(projectPropertyFile.getReportFileName(),projectPropertyFile.getReportFileId());
                }
                projectPropertyFile.setReportFileId(fileId);
                projectPropertyFile.setReportFileName(file.getOriginalFilename());
                break;
        }
        projectPropertyFileRepository.save(projectPropertyFile);
    }

    public void deleteProjectPropertyFile(int id,String type){
        ProjectPropertyFile projectPropertyFile=projectPropertyFileRepository.findById(id).get();
        switch (type){
            case "codeFile":
                fileService.deleteFileByPath(projectPropertyFile.getCodeFileName(),projectPropertyFile.getCodeFileId());
                projectPropertyFile.setCodeFileId(null);
                projectPropertyFile.setCodeFileName(null);
                break;
            case"reportFile":
                fileService.deleteFileByPath(projectPropertyFile.getReportFileName(),projectPropertyFile.getReportFileId());
                projectPropertyFile.setReportFileId(null);
                projectPropertyFile.setReportFileName(null);
                break;
        }
        projectPropertyFileRepository.save(projectPropertyFile);
    }

    public void downloadProjectPropertyFile(int id, String type, HttpServletResponse response){
        ProjectPropertyFile projectPropertyFile=projectPropertyFileRepository.findById(id).get();
        try{
            switch (type){
                case "codeFile":
                    fileService.downloadFile(projectPropertyFile.getCodeFileName(),projectPropertyFile.getCodeFileId(),response);
                    break;
                case"reportFile":
                    fileService.downloadFile(projectPropertyFile.getReportFileName(),projectPropertyFile.getReportFileId(),response);
                    break;
            }
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    public void updateProjectPropertyVersion(int id,ProjectPropertyFileVO projectPropertyFileVO){
        ProjectPropertyFile projectPropertyFile=projectPropertyFileRepository.findById(id).get();
        projectPropertyFile.update(projectPropertyFileVO.getVersion());
        projectPropertyFileRepository.save(projectPropertyFile);
    }

    public void deleteProjectPropertyVersion(int id){
        ProjectPropertyFile projectPropertyFile=projectPropertyFileRepository.findById(id).get();
        if(projectPropertyFile.getCodeFileId()!=null) fileService.deleteFileByPath(projectPropertyFile.getCodeFileName(),
                projectPropertyFile.getCodeFileId());
        if(projectPropertyFile.getReportFileId()!=null) fileService.deleteFileByPath(projectPropertyFile.getReportFileName(),
                projectPropertyFile.getReportFileName());
        projectPropertyFileRepository.delete(projectPropertyFile);
    }

    public void deleteProjectProperty(int id){
        ProjectProperty projectProperty=projectPropertyRepository.findById(id).get();
        if(projectProperty.getProjectPropertyFileList()!=null){
            for(ProjectPropertyFile projectPropertyFile:projectProperty.getProjectPropertyFileList()){
                this.deleteProjectPropertyVersion(projectPropertyFile.getId());
            }
        }
        projectPropertyRepository.delete(projectProperty);
    }

    private String getFileTypeFolderName(String fileType){
        String res=fileType.substring(0,1).toUpperCase()+fileType.substring(1,fileType.length()-4);
        return res;
    }

}

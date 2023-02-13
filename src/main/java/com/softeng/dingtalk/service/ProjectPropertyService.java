package com.softeng.dingtalk.service;
import com.softeng.dingtalk.po.ProjectPropertyPo;
import com.softeng.dingtalk.po.ProjectPropertyFilePo;
import com.softeng.dingtalk.dao.repository.ProjectPropertyFileRepository;
import com.softeng.dingtalk.dao.repository.ProjectPropertyRepository;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

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
        ProjectPropertyPo projectPropertyPo =new ProjectPropertyPo(projectPropertyVO.getName(),projectPropertyVO.getPath());
        projectPropertyRepository.save(projectPropertyPo);
    }

    public void updateProjectProperty(ProjectPropertyVO projectPropertyVO){
        ProjectPropertyPo projectPropertyPo =projectPropertyRepository.findById(projectPropertyVO.getId()).get();
        projectPropertyPo.update(projectPropertyVO.getName(),projectPropertyVO.getPath());
        projectPropertyRepository.save(projectPropertyPo);
    }

    public Map<String, Object> getProjectPropertyList(int page, int size){
        Pageable pageable = PageRequest.of(page-1,size, Sort.by("id").descending());
        Page<ProjectPropertyPo> projectProperties=projectPropertyRepository.findAll(pageable);
        return Map.of("list",projectProperties.toList(),"total",projectProperties.getTotalElements());
    }

    public void addProjectPropertyVersion(MultipartFile codeFile, MultipartFile reportFile,
                                          String name, int id){
        ProjectPropertyPo projectPropertyPo =projectPropertyRepository.findById(id).get();
        String codeFileId=fileService.addFileByPath(codeFile, projectPropertyPo.getPath()+"/"+name+"/Code");
        String reportFileId=fileService.addFileByPath(reportFile, projectPropertyPo.getPath()+"/"+name+"/Report");
        ProjectPropertyFilePo projectPropertyFilePo =new ProjectPropertyFilePo(null,name, projectPropertyPo,codeFile.getOriginalFilename(),
                codeFileId,reportFile.getOriginalFilename(),reportFileId);
        projectPropertyFileRepository.save(projectPropertyFilePo);
    }

    public ProjectPropertyPo getProjectPropertyDetail(int id){
        return projectPropertyRepository.findById(id).get();
    }
    public String addProjectPropertyFile(MultipartFile file,int id,String type){
        ProjectPropertyFilePo projectPropertyFilePo =projectPropertyFileRepository.findById(id).get();
        String fileId=fileService.addFileByPath(file, projectPropertyFilePo.getProjectProperty().getPath()+"/"+
                projectPropertyFilePo.getVersion()+"/"+getFileTypeFolderName(type));
        switch (type){
            case "codeFile":
                if(projectPropertyFilePo.getCodeFileId()!=null){
                    fileService.deleteFileByPath(projectPropertyFilePo.getCodeFileName(), projectPropertyFilePo.getCodeFileId());
                }
                projectPropertyFilePo.setCodeFileId(fileId);
                projectPropertyFilePo.setCodeFileName(file.getOriginalFilename());
                break;
            case"reportFile":
                if(projectPropertyFilePo.getReportFileId()!=null){
                    fileService.deleteFileByPath(projectPropertyFilePo.getReportFileName(), projectPropertyFilePo.getReportFileId());
                }
                projectPropertyFilePo.setReportFileId(fileId);
                projectPropertyFilePo.setReportFileName(file.getOriginalFilename());
                break;
        }
        projectPropertyFileRepository.save(projectPropertyFilePo);
        return fileId;
    }

    public void deleteProjectPropertyFile(int id,String type){
        ProjectPropertyFilePo projectPropertyFilePo =projectPropertyFileRepository.findById(id).get();
        switch (type){
            case "codeFile":
                fileService.deleteFileByPath(projectPropertyFilePo.getCodeFileName(), projectPropertyFilePo.getCodeFileId());
                projectPropertyFilePo.setCodeFileId(null);
                projectPropertyFilePo.setCodeFileName(null);
                break;
            case"reportFile":
                fileService.deleteFileByPath(projectPropertyFilePo.getReportFileName(), projectPropertyFilePo.getReportFileId());
                projectPropertyFilePo.setReportFileId(null);
                projectPropertyFilePo.setReportFileName(null);
                break;
        }
        projectPropertyFileRepository.save(projectPropertyFilePo);
    }

    public void downloadProjectPropertyFile(int id, String type, HttpServletResponse response){
        ProjectPropertyFilePo projectPropertyFilePo =projectPropertyFileRepository.findById(id).get();
        try{
            switch (type){
                case "codeFile":
                    fileService.downloadFile(projectPropertyFilePo.getCodeFileName(), projectPropertyFilePo.getCodeFileId(),response);
                    break;
                case"reportFile":
                    fileService.downloadFile(projectPropertyFilePo.getReportFileName(), projectPropertyFilePo.getReportFileId(),response);
                    break;
            }
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    public void updateProjectPropertyVersion(int id,ProjectPropertyFileVO projectPropertyFileVO){
        ProjectPropertyFilePo projectPropertyFilePo =projectPropertyFileRepository.findById(id).get();
        projectPropertyFilePo.update(projectPropertyFileVO.getVersion());
        projectPropertyFileRepository.save(projectPropertyFilePo);
    }

  
    public void deleteProjectPropertyVersion(int id){
        ProjectPropertyFilePo projectPropertyFilePo =projectPropertyFileRepository.findById(id).get();
        if(projectPropertyFilePo.getCodeFileId()!=null) fileService.deleteFileByPath(projectPropertyFilePo.getCodeFileName(),
                projectPropertyFilePo.getCodeFileId());
        if(projectPropertyFilePo.getReportFileId()!=null) fileService.deleteFileByPath(projectPropertyFilePo.getReportFileName(),
                projectPropertyFilePo.getReportFileName());
        projectPropertyFileRepository.delete(projectPropertyFilePo);
    }

    public void deleteProjectProperty(int id){
        ProjectPropertyPo projectPropertyPo =projectPropertyRepository.findById(id).get();
        if(projectPropertyPo.getProjectPropertyFilePoList()!=null){
            for(ProjectPropertyFilePo projectPropertyFilePo : projectPropertyPo.getProjectPropertyFilePoList()){
                this.deleteProjectPropertyVersion(projectPropertyFilePo.getId());
            }
        }
        projectPropertyRepository.delete(projectPropertyPo);
    }

    private String getFileTypeFolderName(String fileType){
        String res=fileType.substring(0,1).toUpperCase()+fileType.substring(1,fileType.length()-4);
        return res;
    }

}

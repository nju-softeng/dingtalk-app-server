package com.softeng.dingtalk.service;
import com.softeng.dingtalk.po.ProcessFilePo;
import com.softeng.dingtalk.po.ProcessPropertyPo;
import com.softeng.dingtalk.dao.repository.ProcessFileRepository;
import com.softeng.dingtalk.dao.repository.ProcessPropertyRepository;
import com.softeng.dingtalk.dao.repository.UserRepository;
import com.softeng.dingtalk.vo.ProcessPropertyDetailVO;
import com.softeng.dingtalk.vo.ProcessPropertyVO;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class ProcessPropertyService {
    @Autowired
    ProcessFileRepository processFileRepository;
    @Autowired
    ProcessPropertyRepository processPropertyRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    FileService fileService;
    public void addProcessProperty(MultipartFile file, ProcessPropertyVO processPropertyVO,int uid){
        //1.保存文件
        String fileId;
        ProcessFilePo processFilePo =null;
        if(file!=null){
            fileId=fileService.addFileByPath(file,processPropertyVO.getFilePath()+"/PPT");
            processFilePo =new ProcessFilePo(file.getOriginalFilename(),"PPT",fileId);
        }
        //2.保存记录信息
        ProcessPropertyPo processPropertyPo =new ProcessPropertyPo(processPropertyVO.getConferenceName(),processPropertyVO.getYear(),
                processPropertyVO.getFilePath(),userRepository.findById(uid).get());
        processPropertyPo.setPPTFile(processFilePo);
        processPropertyRepository.save(processPropertyPo);
    }
    public Map<String, Object> getProcessProperty(int page, int size){
        Pageable pageable = PageRequest.of(page-1,size, Sort.by("id").descending());
        Page<ProcessPropertyPo> processProperties=processPropertyRepository.findAll(pageable);
        List<ProcessPropertyPo> infoList=processProperties.toList();
        return Map.of("list",infoList,"total",processProperties.getTotalElements());
    }
    public void updateProcessProperty(ProcessPropertyVO processPropertyVO){
        ProcessPropertyPo processPropertyPo =processPropertyRepository.findById(processPropertyVO.getId()).get();
        processPropertyPo.update(processPropertyVO.getConferenceName(),processPropertyVO.getYear());
        processPropertyRepository.save(processPropertyPo);
    }

    public ProcessPropertyDetailVO getProcessPropertyDetail(int id){
        ProcessPropertyPo pp=processPropertyRepository.findById(id).get();
        log.info(String.valueOf(pp.getConferencePhotoFileList().size()));
        log.info(String.valueOf(pp.getPersonalPhotoFileList()));
        return new ProcessPropertyDetailVO(pp.getId(),pp.getConferenceName(),pp.getYear(),pp.getFilePath(),pp.getUser(),
                pp.getInvitationFile(),pp.getPPTFile(),pp.getPersonalPhotoFileList(),pp.getConferencePhotoFileList());
    }

    public void deleteProcessProperty(int id){
        ProcessPropertyPo pp=processPropertyRepository.findById(id).get();
        if(pp.getInvitationFile()!=null)fileService.deleteFileByPath(pp.getInvitationFile().getFileName(),pp.getInvitationFile().getFileId());
        if(pp.getPPTFile()!=null)fileService.deleteFileByPath(pp.getPPTFile().getFileName(),pp.getPPTFile().getFileId());
        if(pp.getPersonalPhotoFileList()!=null)this.simpleDeleteFileList(pp.getPersonalPhotoFileList());
        if(pp.getConferencePhotoFileList()!=null)this.simpleDeleteFileList(pp.getConferencePhotoFileList());
        processPropertyRepository.delete(pp);
    }

    public void addProcessFile(MultipartFile file,String fileType,int id){
        ProcessPropertyPo pp=processPropertyRepository.findById(id).get();
        String fileName=file.getOriginalFilename();
        String fileId=fileService.addFileByPath(file,pp.getFilePath()+"/"+getFileTypeFolderName(fileType));
        ProcessFilePo processFilePo =new ProcessFilePo(fileName,fileType,fileId);
        processFilePo.setProcessProperty(pp);
        List<ProcessFilePo> processFilePoList =null;
        switch (fileType){
            case"invitationFile":
                pp.setInvitationFile(processFilePo);
                break;
            case"PPTFile":
                pp.setPPTFile(processFilePo);
                break;
            case "personalPhotoFile":
                processFilePoList =pp.getPersonalPhotoFileList();
                if(pp.getPersonalPhotoFileList()==null){
                    processFilePoList =new LinkedList<ProcessFilePo>();
                }
                processFilePoList.add(processFilePo);
                processFileRepository.save(processFilePo);
                pp.setPersonalPhotoFileList(processFilePoList);
                break;
            case "conferencePhotoFile":
                processFilePoList =pp.getConferencePhotoFileList();
                if(pp.getConferencePhotoFileList()==null){
                    processFilePoList =new LinkedList<ProcessFilePo>();
                }
                processFilePoList.add(processFilePo);
                processFileRepository.save(processFilePo);
                pp.setConferencePhotoFileList(processFilePoList);
                break;
        }
        processPropertyRepository.save(pp);
    }

    public void deleteProcessFile(int processId, int fileId, String type){
        ProcessPropertyPo pp=processPropertyRepository.findById(processId).get();
        ProcessFilePo pf=processFileRepository.findById(fileId).get();
        fileService.deleteFileByPath(pf.getFileName(),pf.getFileId());
        switch (type){
            case"invitationFile":
                pp.setInvitationFile(null);
                break;
            case"PPTFile":
                pp.setPPTFile(null);
                break;
            case "personalPhotoFile":
                pp.getPersonalPhotoFileList().remove(pf);
                break;
            case "conferencePhotoFile":
                pp.getConferencePhotoFileList().remove(pf);
                break;
        }
        processFileRepository.delete(pf);
        processPropertyRepository.save(pp);
    }

    /**
     * 简单删除文件，不修改数据库
     * @param processFilePoList
     */
    private void simpleDeleteFileList(List<ProcessFilePo> processFilePoList){
        for(ProcessFilePo processFilePo : processFilePoList){
            fileService.deleteFileByPath(processFilePo.getFileName(), processFilePo.getFileId());
        }
    }

    public void downloadProcessFile(int id, HttpServletResponse response)  {
        ProcessFilePo processFilePo =processFileRepository.findById(id).get();
        try{
            fileService.downloadFile(processFilePo.getFileName(), processFilePo.getFileId(),response);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    private String getFileTypeFolderName(String fileType){
        String res=fileType.substring(0,1).toUpperCase()+fileType.substring(1,fileType.length()-4);
        return res;
    }
}

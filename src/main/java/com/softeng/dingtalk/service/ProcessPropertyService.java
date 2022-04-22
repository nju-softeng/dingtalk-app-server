package com.softeng.dingtalk.service;
import com.softeng.dingtalk.entity.EventFile;
import com.softeng.dingtalk.entity.EventProperty;
import com.softeng.dingtalk.entity.ProcessFile;
import com.softeng.dingtalk.entity.ProcessProperty;
import com.softeng.dingtalk.repository.ProcessFileRepository;
import com.softeng.dingtalk.repository.ProcessPropertyRepository;
import com.softeng.dingtalk.repository.UserRepository;
import com.softeng.dingtalk.vo.EventPropertyInfoVO;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        String fileId=fileService.addFileByPath(file,processPropertyVO.getFilePath()+"/PPT");
        ProcessFile processFile=new ProcessFile(file.getOriginalFilename(),"PPT",fileId);
        //2.保存记录信息
        ProcessProperty processProperty=new ProcessProperty(processPropertyVO.getConferenceName(),processPropertyVO.getYear(),
                processPropertyVO.getFilePath(),userRepository.findById(uid).get());
        processProperty.setPPTFile(processFile);
        processPropertyRepository.save(processProperty);
    }
    public Map<String, Object> getProcessProperty(int page, int size){
        Pageable pageable = PageRequest.of(page-1,size, Sort.by("id").descending());
        Page<ProcessProperty> processProperties=processPropertyRepository.findAll(pageable);
        List<ProcessPropertyVO> infoList=processProperties.stream().map(processProperty -> new ProcessPropertyVO(processProperty.getId(),
                processProperty.getConferenceName(),processProperty.getYear(),null)).collect(Collectors.toList());
        return Map.of("list",infoList,"total",processProperties.getTotalElements());
    }
    public void updateProcessProperty(ProcessPropertyVO processPropertyVO){
        ProcessProperty processProperty=processPropertyRepository.findById(processPropertyVO.getId()).get();
        processProperty.update(processPropertyVO.getConferenceName(),processPropertyVO.getYear());
        processPropertyRepository.save(processProperty);
    }

    public ProcessPropertyDetailVO getProcessPropertyDetail(int id){
        ProcessProperty pp=processPropertyRepository.findById(id).get();
        return new ProcessPropertyDetailVO(pp.getId(),pp.getConferenceName(),pp.getYear(),pp.getFilePath(),pp.getUser(),
                pp.getInvitationFile(),pp.getPPTFile(),pp.getPersonalPhotoFileList(),pp.getConferencePhotoFileList());
    }

    public void deleteProcessProperty(int id){
        ProcessProperty pp=processPropertyRepository.findById(id).get();
        if(pp.getInvitationFile()!=null)fileService.deleteFileByPath(pp.getInvitationFile().getFileName(),pp.getInvitationFile().getFileId());
        if(pp.getPPTFile()!=null)fileService.deleteFileByPath(pp.getPPTFile().getFileName(),pp.getPPTFile().getFileId());
        if(pp.getPersonalPhotoFileList()!=null)this.simpleDeleteFileList(pp.getPersonalPhotoFileList());
        if(pp.getConferencePhotoFileList()!=null)this.simpleDeleteFileList(pp.getConferencePhotoFileList());
        processPropertyRepository.delete(pp);
    }

    public void addProcessFile(MultipartFile file,String fileType,int id){
        ProcessProperty pp=processPropertyRepository.findById(id).get();
        String fileName=file.getOriginalFilename();
        String fileId=fileService.addFileByPath(file,pp.getFilePath()+"/"+fileType);
        ProcessFile processFile=new ProcessFile(fileName,fileType,fileId);
        List<ProcessFile> processFileList=null;
        switch (fileType){
            case"Invitation":
                pp.setInvitationFile(processFile);
                break;
            case"PPT":
                pp.setPPTFile(processFile);
                break;
            case "PersonalPhoto":
                processFileList=pp.getPersonalPhotoFileList();
                if(pp.getPersonalPhotoFileList()==null){
                    processFileList=new LinkedList<ProcessFile>();
                }
                processFileList.add(processFile);
                pp.setPersonalPhotoFileList(processFileList);
                break;
            case "ConferencePhoto":
                processFileList=pp.getConferencePhotoFileList();
                if(pp.getConferencePhotoFileList()==null){
                    processFileList=new LinkedList<ProcessFile>();
                }
                processFileList.add(processFile);
                pp.setConferencePhotoFileList(processFileList);
                break;
        }
        processPropertyRepository.save(pp);
    }

    public void deleteProcessFile(int processId, int fileId, String type){
        ProcessProperty pp=processPropertyRepository.findById(processId).get();
        ProcessFile pf=processFileRepository.findById(fileId).get();
        fileService.deleteFileByPath(pf.getFileName(),pf.getFileId());
        switch (type){
            case"Invitation":
                pp.setInvitationFile(null);
                break;
            case"PPT":
                pp.setPPTFile(null);
                break;
            case "PersonalPhoto":
                pp.getPersonalPhotoFileList().remove(pf);
                break;
            case "ConferencePhoto":
                pp.getConferencePhotoFileList().remove(pf);
                break;
        }
        processFileRepository.delete(pf);
        processPropertyRepository.save(pp);
    }

    /**
     * 简单删除文件，不修改数据库
     * @param processFileList
     */
    private void simpleDeleteFileList(List<ProcessFile> processFileList){
        for(ProcessFile processFile: processFileList){
            fileService.deleteFileByPath(processFile.getFileName(),processFile.getFileId());
        }
    }

    public void downloadProcessFile(int id, HttpServletResponse response)  {
        ProcessFile processFile=processFileRepository.findById(id).get();
        try{
            fileService.downloadFile(processFile.getFileName(),processFile.getFileId(),response);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }
}

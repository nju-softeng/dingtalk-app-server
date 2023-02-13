package com.softeng.dingtalk.service;
import com.softeng.dingtalk.po.EventFilePo;
import com.softeng.dingtalk.po.EventPropertyPo;
import com.softeng.dingtalk.dao.repository.EventPropertyRepository;
import com.softeng.dingtalk.dao.repository.EventFileRepository;
import com.softeng.dingtalk.vo.EventPropertyInfoVO;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class EventPropertyService {
    @Autowired
    EventPropertyRepository eventPropertyRepository;
    @Autowired
    EventFileRepository eventFileRepository;
    @Autowired
    FileService fileService;
    @Autowired
    UserService userService;


    /**
     * 获取资产列表
     * @param page
     * @param size
     * @return
     */
    public Map<String, Object> getEventInfoList(int page, int size){
        Pageable pageable = PageRequest.of(page-1,size, Sort.by("id").descending());
        Page<EventPropertyPo> eventProperties=eventPropertyRepository.findAll(pageable);
        List<EventPropertyInfoVO> infoList=eventProperties.stream().map(eventPropertyPo -> new EventPropertyInfoVO(eventPropertyPo.getId(),
                eventPropertyPo.getName(), eventPropertyPo.getYear(), eventPropertyPo.getType())).collect(Collectors.toList());
        return Map.of("list",infoList,"total",eventProperties.getTotalElements());
    }

    public EventPropertyPo getEventInfo(int eventId){
        EventPropertyPo ep = eventPropertyRepository.findById(eventId).get();
        //以下三行代码非常关键，不能删除
        log.info(String.valueOf(ep.getPictureFileList().size()));
        log.info(String.valueOf(ep.getVideoFileList().size()));
        log.info(String.valueOf(ep.getDocFileList().size()));
        return ep;
//        EventProperty ep=eventPropertyRepository.findById(eventId).get();
//        return new EventPropertyInfoVO(ep.getId(),ep.getName(),ep.getYear(),ep.getType());
    }

    /**
     * 添加新的event资产
     * @param eventPropertyPo
     */
    public void addEventProperty(EventPropertyPo eventPropertyPo){
        eventPropertyRepository.save(eventPropertyPo);
    }

    /**
     * 更新event资产
     * @param eventPropertyPo
     */
    public void updateEventProperty(EventPropertyPo eventPropertyPo){
        EventPropertyPo eventPropertyPoRec =eventPropertyRepository.findById(eventPropertyPo.getId()).get();
        eventPropertyPoRec.update(eventPropertyPo.getName(), eventPropertyPo.getYear(), eventPropertyPo.getType());
        eventPropertyRepository.save(eventPropertyPoRec);
    }


    /**
     * 为资产添加文件列表（既可以添加全新文件列表，也可以附加新的文件）
     * @param fileList
     * @param type
     * @param eventId
     */
    public void addEventPropertyFileList(List<MultipartFile> fileList, String type, int eventId){
        EventPropertyPo eventPropertyPo =eventPropertyRepository.findById(eventId).get();
        List<EventFilePo> eventFilePoList =saveFileList(fileList, eventPropertyPo, eventPropertyPo.getPath()+"/"+type,type);
        switch (type){
            case "Picture":
                eventFilePoList =this.appendList(eventPropertyPo.getPictureFileList(), eventFilePoList);
                eventPropertyPo.setPictureFileList(eventFilePoList);
                break;
            case "Video":
                eventFilePoList =this.appendList(eventPropertyPo.getVideoFileList(), eventFilePoList);
                eventPropertyPo.setVideoFileList(eventFilePoList);
                break;
            case "Doc":
                eventFilePoList =this.appendList(eventPropertyPo.getDocFileList(), eventFilePoList);
                eventPropertyPo.setDocFileList(eventFilePoList);
                break;
        }
        eventPropertyRepository.save(eventPropertyPo);
    }

    private List<EventFilePo> appendList(List<EventFilePo> headList, List<EventFilePo> appendList){
        if(headList!=null){
            headList.addAll(appendList);
            return headList;
        }else
            return appendList;
    }

    /**
     * 删除单个资产文件
     * @param eventId
     * @param eventFileId
     * @param type
     */
    public void deleteEventPropertyFile(int eventId, int eventFileId, String type){
        EventPropertyPo eventPropertyPo =eventPropertyRepository.findById(eventId).get();
        EventFilePo eventFilePo =eventFileRepository.findById(eventFileId).get();
        fileService.deleteFileByPath(eventFilePo.getFileName(), eventFilePo.getFileId());
        switch (type){
            case "Picture":
                eventPropertyPo.getPictureFileList().remove(eventFilePo);
                break;
            case "Video":
                eventPropertyPo.getVideoFileList().remove(eventFilePo);
                break;
            case "Doc":
                eventPropertyPo.getDocFileList().remove(eventFilePo);
                break;
        }
        eventFileRepository.delete(eventFilePo);
//        eventPropertyRepository.save(eventProperty);
    }

    public void downloadEventFile(int eventFileId, HttpServletResponse response){
        EventFilePo eventFilePo =eventFileRepository.findById(eventFileId).get();
        try{
            fileService.downloadFile(eventFilePo.getFileName(), eventFilePo.getFileId(),response);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    /**
     * 调用文件保存接口，保存文件列表
     * @param fileList
     * @param path
     * @param type
     * @return
     */
    private List<EventFilePo> saveFileList(List<MultipartFile> fileList, EventPropertyPo eventPropertyPo, String path, String type){
        if(fileList!=null && fileList.size()!=0){
            List<EventFilePo> eventFilePoList =new ArrayList<>();
            for(MultipartFile file:fileList){
                String fileId=fileService.addFileByPath(file,path);
                EventFilePo eventFilePo =new EventFilePo(file.getOriginalFilename(),fileId,type, eventPropertyPo);
                eventFilePoList.add(eventFilePo);
            }
            eventFileRepository.saveBatch(eventFilePoList);
            return eventFilePoList;
        } else {
            log.info("寄");
          return null;
        }
    }

    /**
     * 删除资产
     * @param id
     */
    public void deleteEventProperty(int id){
        EventPropertyPo eventPropertyPo =eventPropertyRepository.findById(id).get();
        List<EventFilePo> eventFilePoList = eventPropertyPo.getDocFileList();
        if(eventFilePoList !=null)this.simpleDeleteFileList(eventFilePoList);
        eventFilePoList = eventPropertyPo.getVideoFileList();
        if(eventFilePoList !=null)this.simpleDeleteFileList(eventFilePoList);
        eventFilePoList = eventPropertyPo.getPictureFileList();
        if(eventFilePoList !=null)this.simpleDeleteFileList(eventFilePoList);
        eventPropertyRepository.deleteById(id);
    }

    /**
     * 简单删除文件，不修改数据库
     * @param eventFilePoList
     */
    private void simpleDeleteFileList(List<EventFilePo> eventFilePoList){
        for(EventFilePo eventFilePo : eventFilePoList){
            fileService.deleteFileByPath(eventFilePo.getFileName(), eventFilePo.getFileId());
        }
    }
}

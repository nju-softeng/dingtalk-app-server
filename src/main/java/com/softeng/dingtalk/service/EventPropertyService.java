package com.softeng.dingtalk.service;

import com.graphbuilder.math.func.EFunction;
import com.softeng.dingtalk.entity.EventFile;
import com.softeng.dingtalk.entity.EventProperty;
import com.softeng.dingtalk.repository.EventPropertyRepository;
import com.softeng.dingtalk.repository.impl.EventFileRepository;
import com.softeng.dingtalk.vo.EventPropertyInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

    public Map<String, Object> getEventInfoList(int page, int size){
        Pageable pageable = PageRequest.of(page-1,size, Sort.by("id").descending());
        Page<EventProperty> eventProperties=eventPropertyRepository.findAll(pageable);
        List<EventPropertyInfoVO> infoList=eventProperties.stream().map(eventProperty -> new EventPropertyInfoVO(eventProperty.getId(),
                eventProperty.getName(),eventProperty.getYear(),eventProperty.getType())).collect(Collectors.toList());
        return Map.of("list",infoList,"total",eventProperties.getTotalElements());
    }

    public void addEventProperty(EventProperty eventProperty, List<MultipartFile> pictureFileList, List<MultipartFile> videoFileList,
                                     List<MultipartFile> docFileList,int uid){

        List<EventFile> fileList=saveFileList(pictureFileList,eventProperty.getPath()+"Picture",uid);
        if(fileList!=null)eventProperty.setPictureFileList(fileList);
        fileList=saveFileList(videoFileList,eventProperty.getPath()+"Video",uid);
        if(fileList!=null)eventProperty.setVideoFileList(fileList);
        fileList=saveFileList(docFileList,eventProperty.getPath()+"Doc",uid);
        if(fileList!=null)eventProperty.setDocFileList(fileList);
        eventPropertyRepository.save(eventProperty);
    }

    private List<EventFile> saveFileList(List<MultipartFile> fileList,String path,int uid){
        if(fileList!=null && fileList.size()!=0){
            List<EventFile> eventFileList=new ArrayList<>();
            String folderId=fileService.getFileFolderId(path,userService.getUserUnionId(uid));
            for(MultipartFile file:fileList){
                String fileId=fileService.addFileByFolderId(file,uid,folderId);
                EventFile eventFile=new EventFile(file.getOriginalFilename(),fileId);
                eventFileList.add(eventFile);
            }
            eventFileRepository.saveBatch(eventFileList);
            return eventFileList;
        } else {
          return null;
        }
    }
//    private void deleteEventFileList(List<>)

    public void updateEventProperty(EventProperty eventProperty){
        EventProperty eventPropertyRec=eventPropertyRepository.findById(eventProperty.getId()).get();
        eventPropertyRec.update(eventProperty.getName(),eventProperty.getYear(),eventProperty.getType());
        eventPropertyRepository.save(eventPropertyRec);
    }

    public void deleteEventProperty(int id){
        List<EventProperty> eventPropertyList=eventPropertyRepository.findAll();

        eventPropertyRepository.deleteById(id);
    }
}

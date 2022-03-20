package com.softeng.dingtalk.service;

import com.softeng.dingtalk.entity.EventProperty;
import com.softeng.dingtalk.repository.EventPropertyRepository;
import com.softeng.dingtalk.vo.EventPropertyInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class EventPropertyService {
    @Autowired
    EventPropertyRepository eventPropertyRepository;

    public List<EventPropertyInfoVO> getEventInfoList(){
        return eventPropertyRepository.findAll().stream().map(eventProperty -> new EventPropertyInfoVO(eventProperty.getId(),
                eventProperty.getName(),eventProperty.getYear(),eventProperty.getType())).collect(Collectors.toList());
    }

    public void addEventProperty(EventProperty eventProperty, List<MultipartFile> pictureFileList, List<MultipartFile> videoFileList,
                                     List<MultipartFile> docFileList){

    }

    public void updateEventProperty(EventProperty eventProperty){
        EventProperty eventPropertyRec=eventPropertyRepository.findById(eventProperty.getId()).get();
        eventPropertyRec.update(eventProperty.getName(),eventProperty.getYear(),eventProperty.getType());
        eventPropertyRepository.save(eventPropertyRec);
    }

    public void deleteEventProperty(int id){
        eventPropertyRepository.deleteById(id);
    }
}

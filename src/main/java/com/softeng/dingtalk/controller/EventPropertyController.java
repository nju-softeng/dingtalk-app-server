package com.softeng.dingtalk.controller;

import com.alibaba.fastjson.JSONObject;
import com.softeng.dingtalk.entity.EventProperty;
import com.softeng.dingtalk.service.EventPropertyService;
import com.softeng.dingtalk.vo.EventPropertyInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class EventPropertyController {
    @Autowired
    EventPropertyService eventPropertyService;


    @GetMapping("/event/page/{page}/{size}")
    public Map<String, Object> getEventInfoList(@PathVariable int page, @PathVariable int size){
        return eventPropertyService.getEventInfoList(page,size);
    }

    @PostMapping("/event")
    public void addEventProperty(@RequestParam String eventPropertyJsonStr){
        EventProperty eventProperty= JSONObject.parseObject(eventPropertyJsonStr,EventProperty.class);
        if(eventProperty.getId()==null){
            eventPropertyService.addEventProperty(eventProperty);
        }else{
            eventPropertyService.updateEventProperty(eventProperty);
        }
    }

    @PostMapping("/event/{eventId}/eventFile")
    public void addEventPropertyFileList(@RequestParam List<MultipartFile> fileList, @RequestParam String fileType,
                                         @PathVariable int eventId){
        eventPropertyService.addEventPropertyFileList(fileList,fileType,eventId);
    }

    @DeleteMapping("/event/{eventId}/eventType/{type}/eventFile/{eventFileId}")
    public void deleteEventPropertyFile(@PathVariable int eventId, @PathVariable int eventFileId, @PathVariable String type){
        eventPropertyService.deleteEventPropertyFile(eventId,eventFileId, type);
    }


    @DeleteMapping("/event/{id}")
    public void deleteEventProperty(@PathVariable int id){
        eventPropertyService.deleteEventProperty(id);
    }
}

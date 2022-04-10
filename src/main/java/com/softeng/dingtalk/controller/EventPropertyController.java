package com.softeng.dingtalk.controller;

import com.alibaba.fastjson.JSONObject;
import com.softeng.dingtalk.entity.EventFile;
import com.softeng.dingtalk.entity.EventProperty;
import com.softeng.dingtalk.service.EventPropertyService;
import com.softeng.dingtalk.vo.EventPropertyInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

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

    @GetMapping("/event/{eventId}")
    public EventProperty getEventInfo(@PathVariable int eventId){
        return eventPropertyService.getEventInfo(eventId);
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
    public void addEventPropertyFileList(@RequestParam MultipartFile eventPropertyFile, @RequestParam String fileType,
                                         @PathVariable int eventId){
        List<MultipartFile> fileList=new LinkedList<>();
        fileList.add(eventPropertyFile);
        eventPropertyService.addEventPropertyFileList(fileList,fileType,eventId);
    }

    @DeleteMapping("/event/{eventId}/eventFile/{eventFileId}/eventFileType/{type}")
    public void deleteEventPropertyFile(@PathVariable int eventId, @PathVariable int eventFileId, @PathVariable String type){
        eventPropertyService.deleteEventPropertyFile(eventId,eventFileId, type);
    }

    @GetMapping("/eventFile/{eventFileId}")
    public void downloadEventFile(@PathVariable int eventFileId, HttpServletResponse response){
        eventPropertyService.downloadEventFile(eventFileId,response);
    }


    @DeleteMapping("/event/{id}")
    public void deleteEventProperty(@PathVariable int id){
        eventPropertyService.deleteEventProperty(id);
    }
}

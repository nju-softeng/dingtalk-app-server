package com.softeng.dingtalk.controller;
import com.alibaba.fastjson.JSONObject;
import com.softeng.dingtalk.po.EventPropertyPo;
import com.softeng.dingtalk.service.EventPropertyService;
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
    public EventPropertyPo getEventInfo(@PathVariable int eventId){
        return eventPropertyService.getEventInfo(eventId);
    }

    @PostMapping("/event")
    public void addEventProperty(@RequestParam String eventPropertyJsonStr){
        EventPropertyPo eventPropertyPo = JSONObject.parseObject(eventPropertyJsonStr, EventPropertyPo.class);
        if(eventPropertyPo.getId()==null){
            eventPropertyService.addEventProperty(eventPropertyPo);
        }else{
            eventPropertyService.updateEventProperty(eventPropertyPo);
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

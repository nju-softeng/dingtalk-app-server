package com.softeng.dingtalk.controller;

import com.alibaba.fastjson.JSONObject;
import com.softeng.dingtalk.po.DissertationPo;
import com.softeng.dingtalk.service.DissertationPropertyService;
import com.softeng.dingtalk.vo.DissertationVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class DissertationPropertyController {
    @Autowired
    DissertationPropertyService dissertationPropertyService;

    @PostMapping("/dissertation")
    public void addDissertation(@RequestParam(value = "file") MultipartFile file,
                                @RequestParam(value = "dissertationVOJsonStr") String dissertationVOJsonStr,
                                @RequestAttribute int uid){
        DissertationVO dissertationVO= JSONObject.parseObject(dissertationVOJsonStr, DissertationVO.class);
        if(dissertationVO.getId()==null){
            dissertationPropertyService.addDissertation(file,dissertationVO);
        }else{
            dissertationPropertyService.updateDissertation(dissertationVO);
        }
    }

    @GetMapping("/dissertation/page/{page}/{size}")
    public Map<String, Object> getDissertationInfo(@PathVariable int page, @PathVariable int size){
        return dissertationPropertyService.getDissertation(page,size);
    }

    @GetMapping("/{uid}/dissertation")
    public DissertationPo getDissertationDetail(@PathVariable int uid){
        return dissertationPropertyService.getDissertationDetail(uid);
    }

    @DeleteMapping("/dissertation/{id}")
    public void deleteDissertation(@PathVariable int id){
        dissertationPropertyService.deleteDissertation(id);
    }

    @PostMapping("/dissertation/{id}")
    public void addDissertationFile(@RequestParam(value = "file") MultipartFile file,@RequestParam String fileType,@PathVariable int id){
        dissertationPropertyService.addDissertationFile(file,fileType,id);
    }

    @DeleteMapping("/dissertation/{id}/file/{type}")
    public void deleteDissertationFile(@PathVariable int id,@PathVariable String type){
        dissertationPropertyService.deleteDissertationFile(id,type);
    }

    @PostMapping("/dissertation/{id}/fileType/{type}")
    public void downloadDissertationFile(@PathVariable int id, @PathVariable String type, HttpServletResponse response){
        dissertationPropertyService.downLoadDissertationFile(id,type,response);
    }
}

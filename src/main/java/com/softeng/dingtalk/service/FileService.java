package com.softeng.dingtalk.service;

import com.softeng.dingtalk.api.BaseApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author RickyWu
 * @description
 * @create 7/3/2022
 */
@Service
@Transactional
@Slf4j
public class FileService {
    @Autowired
    BaseApi baseApi;

    public String addFile(MultipartFile multipartFile, String uid){
        File file=null;
        String id=null;
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            String[] filenameList = originalFilename.split("\\.");
            String fileName="";
            for(int i=0;i<filenameList.length-1;i++){
                fileName=fileName.concat(filenameList[i]);
            }
            file=File.createTempFile(fileName, filenameList[filenameList.length-1]);
            multipartFile.transferTo(file);
            id=baseApi.uploadFile(file,uid);
            log.info("获得存储media_id "+id);
            file.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
            log.info("存储服务出现问题！");
        }
        return id;
    }
}

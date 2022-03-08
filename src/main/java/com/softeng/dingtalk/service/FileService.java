package com.softeng.dingtalk.service;

import com.softeng.dingtalk.api.BaseApi;
import com.softeng.dingtalk.entity.User;
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

    @Autowired
    UserService userService;

    public String addFile(MultipartFile multipartFile, int uid){
        File file=null;
        String fileId=null;
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            file=File.createTempFile(originalFilename, null);
            multipartFile.transferTo(file);
            String unionId=userService.getUserUnionId(uid);
            fileId=baseApi.addFile(file,unionId,originalFilename);
            log.info("获得fileId "+fileId);
            file.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
            log.info("存储服务出现问题！");
        }
        return fileId;
    }
}

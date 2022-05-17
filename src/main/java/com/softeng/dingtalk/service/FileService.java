package com.softeng.dingtalk.service;

import com.aliyun.dingtalkdrive_1_0.models.AddFileResponseBody;
import com.aliyun.dingtalkdrive_1_0.models.GetDownloadInfoResponseBody;
import com.softeng.dingtalk.api.BaseApi;
import com.softeng.dingtalk.vo.PaperFileDownloadInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

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

    @Value("${file.rootPath}")
    private String rootPath;
    public String getFileFolderId(String path, String unionId){
        try{
            String spaceId=baseApi.getSpaceId(unionId);
            String parentId="0";
            if(path.length()==0) return spaceId;
            String pathList[]=path.split("/");
            for(int i=0;i<pathList.length;i++){
                log.info(spaceId);
                String folderId=baseApi.getFolderId(pathList[i],unionId,parentId);
                if(folderId==null){
                    AddFileResponseBody file=baseApi.addFolder(unionId,spaceId,parentId,pathList[i]);
                    parentId=file.getFileId();
                }else{
                    parentId=folderId;
                }
            }
            return parentId;
        }catch (Exception e){
            return null;
        }

    }


    /*
    public String addFileByPath(MultipartFile multipartFile, int uid, String path){
        File file;
        String fileId=null;
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            file=File.createTempFile(originalFilename, null);
            multipartFile.transferTo(file);
            String unionId=userService.getUserUnionId(uid);
            fileId=baseApi.addFile(file,unionId,originalFilename,this.getFileFolderId(path,unionId));
            log.info("获得fileId "+fileId);
            file.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
            log.info("存储服务出现问题！");
        }
        return fileId;

    }
    */

    public String addFileByPath(MultipartFile multipartFile, String path){
        String parentDirPath=(this.rootPath+path);
        log.info("文件夹地址"+parentDirPath);
        File dir = new File(parentDirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String fileName=multipartFile.getOriginalFilename();
        String filePath=parentDirPath+'/'+fileName;
        try{
            FileOutputStream out=new FileOutputStream(filePath);
            out.write(multipartFile.getBytes());
            out.flush();
            out.close();
        }catch (Exception e){
            log.info(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return path;

    }
    public void deleteFileByPath(String fileName,String path){
        File file=new File(rootPath+path+'/'+fileName);
        if(file.exists())file.delete();
    }

    public String addFileByFolderId(MultipartFile multipartFile, int uid, String folderId){
        File file;
        String fileId=null;
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            file=File.createTempFile(originalFilename, null);
            multipartFile.transferTo(file);
            String unionId=userService.getUserUnionId(uid);
            fileId=baseApi.addFile(file,unionId,originalFilename,folderId);
            log.info("获得fileId "+fileId);
            file.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
            log.info("存储服务出现问题！");
        }
        return fileId;
    }

    public void downloadFile(String fileName, String filePath, HttpServletResponse response)throws IOException{
        File file = new File(rootPath+filePath+"/"+fileName);
        response.setContentType("application/force-download");
        response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
        byte[] buffer = new byte[1024];
        FileInputStream fis=null;
        BufferedInputStream bis=null;
        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            OutputStream os = response.getOutputStream();
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
        }catch (Exception e){
            log.info(e.getMessage());
        } finally {
            if(bis!= null) bis.close();
            if(fis!= null) fis.close();
        }

    }
    public PaperFileDownloadInfoVO getPaperFileDownloadInfoVO(String fileId, int uid){
        String unionId=userService.getUserUnionId(uid);
        GetDownloadInfoResponseBody.GetDownloadInfoResponseBodyDownloadInfo fileDownloadInfo=baseApi.getFileDownloadInfo(unionId,fileId);
        PaperFileDownloadInfoVO paperFileDownloadInfoVO=new PaperFileDownloadInfoVO();
        paperFileDownloadInfoVO.setUrl(fileDownloadInfo.getResourceUrl());
        paperFileDownloadInfoVO.setHeaderKey1(fileDownloadInfo.getHeaders().get("Authoration").toString());
        paperFileDownloadInfoVO.setHeaderKey1(fileDownloadInfo.getHeaders().get("Date").toString());
        return paperFileDownloadInfoVO;
    }
}

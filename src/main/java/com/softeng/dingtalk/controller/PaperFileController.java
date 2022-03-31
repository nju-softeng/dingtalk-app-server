package com.softeng.dingtalk.controller;


import com.softeng.dingtalk.service.FileService;
import com.softeng.dingtalk.service.PaperFileService;
import com.softeng.dingtalk.service.PaperService;
import com.softeng.dingtalk.vo.PaperFileDownloadInfoVO;
import com.softeng.dingtalk.vo.PaperFileInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @author RickyWu
 * @description
 * @create 10/3/2022
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class PaperFileController {
    @Autowired
    PaperService paperService;
    @Autowired
    PaperFileService paperFileService;
    @Autowired
    FileService fileService;

    /**
     * 上传论文文件
     * @param file
     * @param fileType
     * @return
     */
    @PostMapping("{uid}/paperFile/{paperId}")
    public void addPaperFile(@PathVariable int paperId, @PathVariable int uid, @RequestParam MultipartFile file, @RequestParam String fileType){
        paperFileService.addPaperFile(paperId,uid,file,fileType);
    }

    /**
     * 上传外部评审论文文件
     * @param file
     * @param fileType
     * @return
     */
    @PostMapping("{uid}/ex-paperFile/{paperId}")
    public void addExternalPaperFile(@PathVariable int paperId, @PathVariable int uid, @RequestParam MultipartFile file, @RequestParam String fileType){
        paperFileService.addExternalPaperFile(paperId,uid,file,fileType);
    }

    /**
     * 获取论文文件
     * @param fileId
     * @return
     */
    @GetMapping("{uid}/paperFile/{fileId}")
    public PaperFileDownloadInfoVO getPaperFileDownloadInfo(@PathVariable String fileId, @PathVariable int uid){
        return paperFileService.getPaperFileDownloadInfo(uid,fileId);
    }

    /**
     * 获取论文文件信息
     * @param paperId
     * @return
     */
    @GetMapping("{uid}/paperFileInfo/{paperId}")
    public PaperFileInfoVO getPaperFileInfo(@PathVariable int paperId){
        return paperFileService.getPaperFileInfo(paperId);
    }

    /**
     * 获取外部评审论文文件信息
     * @param paperId
     * @return
     */
    @GetMapping("{uid}/ex-paperFileInfo/{paperId}")
    public PaperFileInfoVO getExternalPaperFileInfo(@PathVariable int paperId){
        return paperFileService.getExternalPaperFileInfo(paperId);
    }

    /**
     * 删除论文文件
     * @param uid
     * @param fileId
     * @param fileType
     * @param paperId
     */
    @DeleteMapping("{uid}/paper/{paperId}/paperFile")
    public void deletePaperFile(@PathVariable int uid, @RequestParam String fileId, @RequestParam String fileType,@RequestParam String fileName, @PathVariable int paperId){
        paperFileService.deletePaperFile(fileName,fileId,paperId,fileType);
    }

    /**
     * 删除外部评审论文文件
     * @param uid
     * @param fileId
     * @param fileType
     * @param paperId
     */
    @DeleteMapping("{uid}/ex-paper/{paperId}/ex-paperFile")
    public void deleteExternalPaperFile(@PathVariable int uid, @RequestParam String fileId, @RequestParam String fileType,@RequestParam String fileName, @PathVariable int paperId){
        paperFileService.deleteExternalPaperFile(fileName,fileId,paperId,fileType);
    }


    /**
     * 论文文件下载
     * @param fileName
     * @param filePath
     * @param httpServletResponse
     * @throws IOException
     */
    @PostMapping("/paper/paperFileDownload")
    public void downloadPaperFile(@RequestParam String fileName,@RequestParam String filePath, HttpServletResponse httpServletResponse) throws IOException {
        fileService.downloadFile(fileName, filePath,httpServletResponse);
    }

}

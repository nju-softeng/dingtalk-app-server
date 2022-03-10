package com.softeng.dingtalk.vo;

import lombok.Data;

@Data
public class PaperFileDownloadInfoVO {
    String url;
    String headerKey1;
    String headerKey2;
    public PaperFileDownloadInfoVO(String url, String headerKey1, String headerKey2){
        this.url=url;
        this.headerKey1=headerKey1;
        this.headerKey2=headerKey2;
    }
    public PaperFileDownloadInfoVO() {
    }
}

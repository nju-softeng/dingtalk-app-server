package com.softeng.dingtalk.api;

import com.aliyun.dingtalkdrive_1_0.models.*;
import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.comm.Protocol;
import com.aliyun.oss.model.Callback;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;

import com.dingtalk.api.request.OapiGetJsapiTicketRequest;
import com.dingtalk.api.request.OapiGettokenRequest;

import com.github.benmanes.caffeine.cache.Cache;
import com.softeng.dingtalk.vo.PaperFileDownloadInfoVO;
import com.taobao.api.ApiException;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.TaobaoResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.BufferedSink;
import okio.Okio;
import okio.Sink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;


/**
 * 调用钉钉 sdk 的抽象类, 对调用api的过程进行封装
 * @date: 2021/4/23 16:59
 */
@Slf4j
@Component
public class BaseApi {

    protected static String CORPID;        // 钉钉组织的唯一id
    protected static String APP_KEY;       // 钉钉微应用的 key
    protected static String APP_SECRET;    // 钉钉微应用的密钥
    protected static Long AGENTID;         // 钉钉微应用的 AgentId
    protected static String CHAT_ID;       // 发送群消息的群id
    protected static String DOMAIN;        // 该服务器的域名，用于调用api时鉴权
    private static final String MD5="md5";
    @Value("${my.corpid}")
    public void setCORPID(String corpid) {
        CORPID = corpid;
    }

    @Value("${my.app_key}")
    public void setAppKey(String appKey) {
        APP_KEY = appKey;
    }

    @Value("${my.app_secret}")
    public void setAppSecret(String appSecret) {
        APP_SECRET = appSecret;
    }

    @Value("${my.chat_id}")
    public void setChatId(String chatId) {
        CHAT_ID = chatId;
    }

    @Value("${my.agent_id}")
    public void setAGENTID(Long agentid) {
        AGENTID = agentid;
    }

    @Value("${my.domain}")
    public void setDOMAIN(String domain) {
        DOMAIN = domain;
    }

    /**
     * 注入 Caffeine 缓存
     */
    @Autowired
    Cache<String, String> cache;

    /**
     * 使用 Token 初始化账号Client
     * @return Client
     * @throws Exception
     */
    private com.aliyun.dingtalkdrive_1_0.Client createClient() throws Exception {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        return new com.aliyun.dingtalkdrive_1_0.Client(config);
    }

    /**
     * 执行封装好的请求, 需要accessToken
     * @param request 封装好的请求对象
     * @param url 请求api的地址
     * @param <T> 接受到的响应对象
     * @return
     */
    public <T extends TaobaoResponse> T executeRequest(TaobaoRequest<T> request, String url) {
        DingTalkClient client = new DefaultDingTalkClient(url);
        try {
            return client.execute(request, getAccessToken());
        } catch (ApiException e) {
            e.printStackTrace();
            throw new RuntimeException("请求钉钉服务器出现异常，请管理员登录钉钉开发者后端检查配置~");
        }
    }

    /**
     * 执行封装好的请求, 不需要accessToken
     * @param request 封装好的请求对象
     * @param url 请求api的地址
     * @param <T> 接受到的响应对象
     * @return
     */
    public <T extends TaobaoResponse> T executeRequestWithoutToken(TaobaoRequest<T> request, String url) {
        DingTalkClient client = new DefaultDingTalkClient(url);
        try {
            return client.execute(request);
        } catch (ApiException e) {
            e.printStackTrace();
            throw new RuntimeException("请求钉钉服务器出现异常，请管理员登录钉钉开发者后端检查配置~");
        }
    }

    /**
     * 获取调用钉钉api所需的 AccessToken，获取后会缓存起来，过期之后再重新获取
     * @return java.lang.String
     **/
    public String getAccessToken() {
        String res = cache.asMap().get("AccessToken");
        if (res == null) {
            OapiGettokenRequest request = new OapiGettokenRequest();
            request.setAppkey(APP_KEY);
            request.setAppsecret(APP_SECRET);
            request.setHttpMethod("GET");
            res = executeRequestWithoutToken(request, "https://oapi.dingtalk.com/gettoken").getAccessToken();

            log.info("重新获取 AccessToken 时间: {}", LocalDateTime.now());
            cache.put("AccessToken", res);
        }
        return res;
    }


    /**
     * 获取钉钉前端鉴权所需的 Jsapi Ticket，获取后会缓存起来，过期之后再重新获取
     * @return java.lang.String
     **/
    public String getJsapiTicket() {
        String res = cache.asMap().get("JsapiTicket");
        if (res == null) {
            OapiGetJsapiTicketRequest request = new OapiGetJsapiTicketRequest();
            request.setTopHttpMethod("GET");
            res = executeRequest(request, "https://oapi.dingtalk.com/get_jsapi_ticket").getTicket();

            cache.put("JsapiTicket", res);
            log.info("重新获取 JsapiTicket 时间: {}", LocalDateTime.now());
        }
        return res;
    }

    /**
     * 字节数组转化成十六进制字符串
     * @param hash
     * @return
     */
    private String bytesToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    /**
     * 计算鉴权 signature
     * @param ticket
     * @param nonceStr
     * @param timeStamp
     * @param url
     * @return
     */
    private String sign(String ticket, String nonceStr, long timeStamp, String url)  {
        String plain = "jsapi_ticket=" + ticket + "&noncestr=" + nonceStr + "&timestamp=" + String.valueOf(timeStamp) + "&url=" + url;
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            sha1.reset();
            sha1.update(plain.getBytes("UTF-8"));
            return bytesToHex(sha1.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回鉴权结果
     * @param url
     * @return
     */
    public Map authentication(String url) {
        long timeStamp = System.currentTimeMillis();
        String nonceStr = "todowhatliesclearathand";
        String signature = sign(getJsapiTicket(),nonceStr, timeStamp, url);
        return Map.of("agentId", AGENTID,"url", url, "nonceStr", nonceStr, "timeStamp", timeStamp, "corpId", CORPID, "signature", signature);
    }

    /**
     * 获取云盘空间的Id
     * @param unionId
     * @return java.lang.String
     */
    public String getSpaceId(String unionId) throws Exception {
        ListSpacesHeaders listSpacesHeaders = new ListSpacesHeaders();
        listSpacesHeaders.xAcsDingtalkAccessToken = this.getAccessToken();
        ListSpacesRequest listSpacesRequest = new ListSpacesRequest()
                .setUnionId(unionId)
                .setSpaceType("org")
                .setNextToken("")
                .setMaxResults(50);
        try{
            com.aliyun.dingtalkdrive_1_0.Client client = this.createClient();
            ListSpacesResponse listSpacesResponse=client.listSpacesWithOptions(listSpacesRequest, listSpacesHeaders, new RuntimeOptions());
            return listSpacesResponse.getBody().getSpaces().get(0).getSpaceId();
        }catch (Exception e){
            log.info("getSpaceId WRONG! "+e.getMessage());
            return null;
        }

    }

    /**
     * 获取文件上传信息
     * @param unionId
     * @param mediaId
     * @param file
     * @return java.lang.String
     */
    public GetUploadInfoResponseBody.GetUploadInfoResponseBodyStsUploadInfo getFileUploadInfo(String unionId, String mediaId, File file,String fileName) {
        GetUploadInfoHeaders getUploadInfoHeaders = new GetUploadInfoHeaders();
        getUploadInfoHeaders.xAcsDingtalkAccessToken = this.getAccessToken();
        GetUploadInfoRequest getUploadInfoRequest = new GetUploadInfoRequest()
                .setUnionId(unionId)
                .setFileName(fileName)
                .setFileSize(file.length())
                .setMd5(MD5)
                .setAddConflictPolicy("autoRename");
        if(mediaId!=null){
            getUploadInfoRequest.setMediaId(mediaId);
        }
        log.info("fileName: "+file.getName());
        try{
            com.aliyun.dingtalkdrive_1_0.Client client = this.createClient();
            GetUploadInfoResponse getUploadInfoResponse=client.getUploadInfoWithOptions(this.getSpaceId(unionId), "0", getUploadInfoRequest, getUploadInfoHeaders, new RuntimeOptions());
            return getUploadInfoResponse.getBody().getStsUploadInfo();
        }catch (Exception e){
            log.info("getUploadInfo WRONG! "+e.getMessage());

            return null;
        }

    }

    /**
     * 返回文件mediaId
     * @param file
     * @param unionId
     * @return
     */
    public String uploadFile(File file,String unionId,
                             GetUploadInfoResponseBody.GetUploadInfoResponseBodyStsUploadInfo uploadInfo){
        CredentialsProvider credentialsProvider = new DefaultCredentialProvider(uploadInfo.getAccessKeyId(), uploadInfo.getAccessKeySecret(), uploadInfo.getAccessToken());
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setProtocol(Protocol.HTTPS);
        OSSClient ossClient = new OSSClient(uploadInfo.getEndPoint(), credentialsProvider, clientConfiguration);
        PutObjectRequest putObjectRequest = new PutObjectRequest(uploadInfo.getBucket(), uploadInfo.getMediaId(), file);
        ossClient.putObject(putObjectRequest);
        ossClient.shutdown();
        return uploadInfo.getMediaId();
    }

    /**
     * 返回文件Id
     * @param file
     * @param unionId
     * @return
     */
    public String addFile(File file, String unionId, String fileName){
        GetUploadInfoResponseBody.GetUploadInfoResponseBodyStsUploadInfo uploadInfo=this.getFileUploadInfo(unionId,null,file,fileName);
        this.uploadFile(file,unionId,uploadInfo);
        AddFileHeaders addFileHeaders = new AddFileHeaders();
        addFileHeaders.xAcsDingtalkAccessToken = this.getAccessToken();
        AddFileRequest addFileRequest = new AddFileRequest()
                .setParentId("0")
                .setFileType("file")
                .setFileName(fileName)
                .setMediaId(uploadInfo.getMediaId())
                .setAddConflictPolicy("autoRename")
                .setUnionId(unionId);
        try{
            com.aliyun.dingtalkdrive_1_0.Client client = this.createClient();
            AddFileResponse addFileResponse=client.addFileWithOptions(this.getSpaceId(unionId), addFileRequest, addFileHeaders, new RuntimeOptions());
            return addFileResponse.getBody().getFileId();
        }catch (Exception e){
            log.info("addFile WRONG!");
            return null;
        }
    }

    /**
     * 删除文件
     * @param fileId
     * @param unionId
     */
    public void deleteFile(String fileId,String unionId) {
        DeleteFileHeaders deleteFileHeaders = new DeleteFileHeaders();
        deleteFileHeaders.xAcsDingtalkAccessToken = this.getAccessToken();
        DeleteFileRequest deleteFileRequest = new DeleteFileRequest()
                .setUnionId(unionId)
                .setDeletePolicy("toRecycle");
        try{
            com.aliyun.dingtalkdrive_1_0.Client client=this.createClient();
            client.deleteFileWithOptions(this.getSpaceId(unionId), fileId, deleteFileRequest, deleteFileHeaders, new RuntimeOptions());
        }catch (Exception e){
            log.info(e.getMessage());
        }
    }

    /**
     * @param unionId
     * @param fileId
     * @return
     */
    public GetDownloadInfoResponseBody.GetDownloadInfoResponseBodyDownloadInfo getFileDownloadInfo(String unionId, String fileId){
        GetDownloadInfoHeaders getDownloadInfoHeaders = new GetDownloadInfoHeaders();
        getDownloadInfoHeaders.xAcsDingtalkAccessToken = this.getAccessToken();
        GetDownloadInfoRequest getDownloadInfoRequest = new GetDownloadInfoRequest()
                .setUnionId(unionId);
        try {
            com.aliyun.dingtalkdrive_1_0.Client client = this.createClient();
            GetDownloadInfoResponse getDownloadInfoResponse = client.getDownloadInfoWithOptions(this.getSpaceId(unionId),
                    fileId, getDownloadInfoRequest, getDownloadInfoHeaders, new RuntimeOptions());
            return getDownloadInfoResponse.getBody().getDownloadInfo();
        }catch (Exception e){
            log.info(e.getMessage());
            return null;
        }
    }

}

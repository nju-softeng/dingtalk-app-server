package com.softeng.pms.dingtalk;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGetJsapiTicketRequest;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiGetJsapiTicketResponse;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.taobao.api.ApiException;
import com.taobao.api.TaobaoRequest;
import com.taobao.api.TaobaoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
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
     * 缓存的token信息, spring bean 默认是单例模式
     * 包含了：accessToken, tokenTime, jsapiTicket, ticketTime
     */
    @Autowired
    TokenCache tokenCache;

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
        long curTime = System.currentTimeMillis();
        if (tokenCache.accessToken == null || curTime - tokenCache.tokenTime >= tokenCache.cacheTime ) {
            OapiGettokenRequest request = new OapiGettokenRequest();
            request.setAppkey(APP_KEY);
            request.setAppsecret(APP_SECRET);
            request.setHttpMethod("GET");

            OapiGettokenResponse response = executeRequestWithoutToken(request, "https://oapi.dingtalk.com/gettoken");

            tokenCache.accessToken = response.getAccessToken();
            tokenCache.tokenTime = System.currentTimeMillis();

            log.info("重新获取 AccessToken");
        }
        return tokenCache.accessToken;
    }

    /**
     * 获取钉钉前端鉴权所需的 Jsapi Ticket，获取后会缓存起来，过期之后再重新获取
     * @return java.lang.String
     **/
    public String getJsapiTicket() {
        long curTime = System.currentTimeMillis();
        if (tokenCache.jsapiTicket == null || curTime - tokenCache.ticketTime >= tokenCache.cacheTime) {
            OapiGetJsapiTicketRequest request = new OapiGetJsapiTicketRequest();
            request.setTopHttpMethod("GET");

            OapiGetJsapiTicketResponse response = executeRequest(request, "https://oapi.dingtalk.com/get_jsapi_ticket");
            tokenCache.jsapiTicket = response.getTicket();
            tokenCache.ticketTime = System.currentTimeMillis();

            log.info("JsapiTicket 快要过期，重新获取");
        }
        return tokenCache.jsapiTicket;
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
        String plain = "jsapi_ticket=" + ticket + "&noncestr=" + nonceStr + "&timestamp=" + String.valueOf(timeStamp)
                + "&url=" + url;
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
}

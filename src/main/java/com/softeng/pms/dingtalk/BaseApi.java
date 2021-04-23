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


/**
 * @date: 2021/4/23 16:59
 */
@Slf4j
public abstract class BaseApi {
    protected static String CORPID;
    protected static String APP_KEY;
    protected static String APP_SECRET;
    protected static String CHAT_ID;
    protected static Long AGENTID;
    protected static String DOMAIN;

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

    @Autowired
    TokenCache tokenCache;

    /**
     * 执行封装好的请求, 需要accessToken
     * @param request
     * @param url
     * @param <T>
     * @return
     * @throws ApiException
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
     * @param request
     * @param url
     * @param <T>
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
     * 获取 AccessToken
     * @return java.lang.String
     * @Date 9:10 PM 11/13/2019
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

            log.debug("AccessToken 快要过期，重新获取");
        }
        return tokenCache.accessToken;
    }


    /**
     * 获取 Jsapi Ticket
     * @return java.lang.String
     * @Date 8:20 AM 2/23/2020
     **/
    public String getJsapiTicket() {
        long curTime = System.currentTimeMillis();
        if (tokenCache.jsapiTicket == null || curTime - tokenCache.ticketTime >= tokenCache.cacheTime) {
            OapiGetJsapiTicketRequest request = new OapiGetJsapiTicketRequest();
            request.setTopHttpMethod("GET");

            OapiGetJsapiTicketResponse response = executeRequest(request, "https://oapi.dingtalk.com/get_jsapi_ticket");
            tokenCache.jsapiTicket = response.getTicket();
            tokenCache.ticketTime = System.currentTimeMillis();

            log.debug("JsapiTicket 快要过期，重新获取");
        }
        return tokenCache.jsapiTicket;
    }
}

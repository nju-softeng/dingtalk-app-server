package com.softeng.dingtalk.component;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiUserGetRequest;
import com.dingtalk.api.request.OapiUserGetuserinfoRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiUserGetResponse;
import com.dingtalk.api.response.OapiUserGetuserinfoResponse;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author zhanyeye
 * @description DingTalk 服务端API 工具组件
 * @date 11/13/2019
 */
@Slf4j
@Component
public class DingTalkUtils {
    private static final String APP_KEY = "dingk9nmede0wzi7aywt";
    private static final String APP_SECRET = "nh3mY7PPMAne3aDEpyANGjKlQIFLBkPQ0npYUnOELNVSJFuKST-ngsrfMK2sZiB9";

    /**
     * @Date 9:10 PM 11/13/2019
     * @Description 获取 access_token
     * @Param []
     * @return java.lang.String
     **/
    public String getAccessToken() {
        String token = null;
        DefaultDingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
        OapiGettokenRequest request = new OapiGettokenRequest();
        request.setAppkey(APP_KEY);
        request.setAppsecret(APP_SECRET);
        request.setHttpMethod("GET");
        try {
            OapiGettokenResponse response = client.execute(request);
            token = response.getAccessToken();
        } catch (ApiException e) {
            log.error("getAccessToken failed", e);
            throw new RuntimeException();
        }
        return token;
    }

    /**
     * @Date 9:40 PM 11/13/2019
     * @Description 获得userid,通过 access_token 和 requestAuthcode；在内部调用了getAccessToken()，不用传参
     * @Param [requestAuthCode]
     * @return java.lang.String
     **/
    public String getUserId(String requestAuthCode) {
        String userId = null;
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/getuserinfo");
        OapiUserGetuserinfoRequest request = new OapiUserGetuserinfoRequest();
        request.setCode(requestAuthCode);
        request.setHttpMethod("GET");
        try {
            OapiUserGetuserinfoResponse response = client.execute(request, getAccessToken());
            userId = response.getUserid();
        } catch (ApiException e) {
            log.error("getAccessToken failed", e);
            throw new RuntimeException();
        }
        if (userId == null) {
            throw new RuntimeException("不存在的临时授权码");
        }
        return userId;
    }

    public Map getUserDetail(String userid) {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/get");
        OapiUserGetRequest request = new OapiUserGetRequest();
        request.setUserid(userid);
        request.setHttpMethod("GET");
        OapiUserGetResponse response;
        try {
            response = client.execute(request, getAccessToken());
        } catch (ApiException e) {
            log.error("getUserDetail fail", e);
            throw new RuntimeException();
        }
        return Map.of("name", response.getName(), "isAdmin", response.getIsAdmin(), "position", response.getPosition());
    }

}

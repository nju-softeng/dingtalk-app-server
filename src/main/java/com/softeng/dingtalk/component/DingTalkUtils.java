package com.softeng.dingtalk.component;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.softeng.dingtalk.entity.User;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    private static final String TEAM_ID = "BYQN3886";

    private static String accessToken;

    /**
     * 获取并保存 access_token
     * @param
     * @return void
     * @Date 4:04 PM 2/6/2020
     **/
    private void setAccessToken() {
        accessToken = getAccessToken();
    }



    /**
     * 获取 AccessToken
     * @return java.lang.String
     * @Date 9:10 PM 11/13/2019
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
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "DingtalkUtils 获取accesstoken失败");
        }
        return token;
    }


    /**
     * 获得userid : 通过 access_token 和 requestAuthcode；在内部调用了getAccessToken()，不用传参
     * @param requestAuthCode
     * @return java.lang.String
     * @Date 5:07 PM 1/13/2020
     **/
    public String getUserId(String requestAuthCode) {
        String userId = null;
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/getuserinfo");
        OapiUserGetuserinfoRequest request = new OapiUserGetuserinfoRequest();
        request.setCode(requestAuthCode);
        request.setHttpMethod("GET");
        try {
            OapiUserGetuserinfoResponse response = client.execute(request, accessToken);
            if (!response.isSuccess()) {
                accessToken = getAccessToken();
                response = client.execute(request, accessToken);
            }
            userId = response.getUserid();
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return userId;
    }

    /**
     * 通过 userid （钉钉的用户码），获取钉钉中用户信息
     * @param userid
     * @return com.softeng.dingtalk.entity.User
     * @Date 5:09 PM 1/13/2020
     **/
    public User getNewUser(String userid) {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/get");
        OapiUserGetRequest request = new OapiUserGetRequest();
        request.setUserid(userid);
        request.setHttpMethod("GET");
        OapiUserGetResponse response;
        try {
            response = client.execute(request, accessToken);
            if (!response.isSuccess()) {
                setAccessToken();
                response = client.execute(request, accessToken);
            }
        } catch (ApiException e) {
            log.error("getUserDetail fail", e);
            throw new RuntimeException();
        }
        int authority = response.getIsAdmin() ? User.ADMIN_AUTHORITY : User.USER_AUTHORITY;
        User user = new User(response.getUserid(), response.getName(), response.getAvatar(), authority);
        return  user;
    }


    /**
     * 异步方法
     * 通过 userid 获取用户周报，（uid只是为了在返回值中添加）
     * @param userid, dateTime, uid
     * @return java.util.concurrent.Future<java.util.Map>
     * @Date 7:32 PM 1/23/2020
     **/
    @Async
    public Future<Map> getReports(String userid, LocalDate date, int uid){
        //todo 注意配置公网IP
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/report/list");
        OapiReportListRequest request = new OapiReportListRequest();
        request.setUserid(userid);
        Long startTime = LocalDateTime.of(date, LocalTime.of(12,0)).toInstant(ZoneOffset.of("+8")).toEpochMilli();
        request.setStartTime(startTime); //开始时间
        request.setEndTime(startTime + TimeUnit.DAYS.toMillis(5));  //结束时间
        request.setCursor(0L);
        request.setSize(1L);
        OapiReportListResponse response;
        try {
            response = client.execute(request, accessToken);
            if (!response.isSuccess()) {
                setAccessToken();
                response = client.execute(request, accessToken);
            }
        } catch (ApiException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "获取accesstoken失败");
        }

        if (response.getResult().getDataList().size() == 0) {
            return new AsyncResult<>(Map.of("uid", uid));
        } else {
            List<OapiReportListResponse.JsonObject> contents = response.getResult().getDataList().get(0).getContents().stream()
                    .filter((item) -> !item.getValue().isEmpty())
                    .collect(Collectors.toList());
            return new AsyncResult<>(Map.of("uid", uid,"contents", contents));
        }
    }


    public Map getReport(String userid, LocalDate date) {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/report/list");
        OapiReportListRequest request = new OapiReportListRequest();
        request.setUserid(userid);
        Long startTime = LocalDateTime.of(date, LocalTime.of(12,0)).toInstant(ZoneOffset.of("+8")).toEpochMilli();
        request.setStartTime(startTime); //开始时间
        request.setEndTime(startTime + TimeUnit.DAYS.toMillis(5));  //结束时间
        request.setCursor(0L);
        request.setSize(1L);
        OapiReportListResponse response;
        try {
            response = client.execute(request, accessToken);
            if (!response.isSuccess()) {
                setAccessToken();
                response = client.execute(request, accessToken);
            }
        } catch (ApiException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "获取accesstoken失败");
        }
        if (response.getResult().getDataList().size() == 0) { // 无数据
            return Map.of();
        } else {
            List<OapiReportListResponse.JsonObject> contents = response.getResult().getDataList().get(0).getContents().stream()
                    .filter((item) -> !item.getValue().isEmpty())
                    .collect(Collectors.toList());
            return Map.of("contents", contents);
        }
    }

    //获取整个部门的userid
    public List<String> listUserId() {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/getDeptMember");
        OapiUserGetDeptMemberRequest req = new OapiUserGetDeptMemberRequest();
        req.setDeptId("1");
        req.setHttpMethod("GET");
        OapiUserGetDeptMemberResponse response;
        try {
            response = client.execute(req, accessToken);
            if(!response.isSuccess()) {
                setAccessToken();
                log.debug("????");
                response = client.execute(req, accessToken);
            }
        } catch (ApiException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "获取getUserIds失败");
        }

        return response.getUserIds();
    }





    public void sentGroupMessage() {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/chat/send");
        OapiChatSendRequest request = new OapiChatSendRequest();
        request.setChatid("chat227956d471b69962f8daf4989986f9a0");
        OapiChatSendRequest.ActionCard actionCard = new OapiChatSendRequest.ActionCard();

        actionCard.setTitle("下午好，打扰了，这是一个测试标题");
        actionCard.setMarkdown("markdown 内容，今天雨夹雪，雨夹雪，雨夹雪，\n  ###### 6号标题\n + 1 + 2 + 3，");
        actionCard.setBtnOrientation("1");

        OapiChatSendRequest.BtnJson btn1 = new OapiChatSendRequest.BtnJson();
        btn1.setTitle("test");
        btn1.setActionUrl("http://www.baidu.com");

        OapiChatSendRequest.BtnJson btn2 = new OapiChatSendRequest.BtnJson();
        btn2.setTitle("PC端");
        btn2.setActionUrl("dingtalk://dingtalkclient/action/openapp?corpid=dingeff939842ad9207f35c2f4657eb6378f&container_type=work_platform&app_id=0_313704868&redirect_type=jump&redirect_url=http://www.dingdev.xyz:8080/paper/vote/2");

        List<OapiChatSendRequest.BtnJson> btnJsonList = new ArrayList<>();

        btnJsonList.add(btn1);
        btnJsonList.add(btn2);

        actionCard.setBtnJsonList(btnJsonList);

        request.setActionCard(actionCard);
        request.setMsgtype("action_card");

        try {
            OapiChatSendResponse response = client.execute(request, accessToken);

            if (!response.isSuccess()) {
                setAccessToken();
                response = client.execute(request, accessToken);;
            }
            log.debug(response.getBody());
        } catch (ApiException e) {
            e.printStackTrace();
        }


    }




    public void workrecord(String userid) {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/workrecord/add");
        OapiWorkrecordAddRequest req = new OapiWorkrecordAddRequest();
        req.setUserid(userid);
        req.setCreateTime(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());
        req.setTitle("提交绩效信息");
        req.setUrl("http://www.dingdev.xyz:8080/?corpId=dingeff939842ad9207f35c2f4657eb6378f#/example/table");
        List<OapiWorkrecordAddRequest.FormItemVo> list2 = new ArrayList<OapiWorkrecordAddRequest.FormItemVo>();
        OapiWorkrecordAddRequest.FormItemVo obj3 = new OapiWorkrecordAddRequest.FormItemVo();
        list2.add(obj3);
        obj3.setTitle("标题");
        obj3.setContent("内容");
        req.setFormItemList(list2);
        OapiWorkrecordAddResponse rsp = null;
        try {
            rsp = client.execute(req, getAccessToken());
        } catch (Exception e) {

        }

        System.out.println(rsp.getBody());
    }


//    public Map gettest() {
//        DefaultDingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/get_jsapi_ticket");
//        OapiGetJsapiTicketRequest req = new OapiGetJsapiTicketRequest();
//        req.setTopHttpMethod("GET");
//        OapiGetJsapiTicketResponse response;
//        try {
//            response = client.execute(req, accessToken);
//            if(!response.isSuccess()) {
//                setAccessToken();
//                response = client.execute(req, accessToken);
//            }
//        } catch (ApiException e) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "获取jsapi_ticket失败");
//        }
//
//        Map<String, String> map = new
//
//        return Map.of("agentId", "", "corpId", "", "timeStamp", "", "nonceStr", "", "signature", "");
//    }


}

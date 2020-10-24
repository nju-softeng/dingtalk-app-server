package com.softeng.dingtalk.component;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.*;
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
    private static String CORPID;
    private static String APP_KEY;
    private static String APP_SECRET;
    private static String CHAT_ID;
    private static String AGENTID;
    private static String DOMAIN;

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
    public void setAGENTID(String agentid) {
        AGENTID = agentid;
    }
    @Value("${my.domain}")
    public void setDOMAIN(String domain) {
        DOMAIN = domain;
    }

    /**
     * 缓存时间 1小时 50分钟
     */
    private static final long cacheTime = 1000 * 60 * 55 * 2;
    /**
     * 缓存的accessToken: 不可直接调用，以防过期
     */
    private static String accessToken;
    /**
     * 缓存时间
     */
    private static long tokenTime;
    /**
     * 缓存的accessToken jsapi_ticket: 不可直接调用，以防过期
     */
    private static String jsapiTicket;
    /**
     * 缓存时间
     */
    private static long ticketTime;


    /**
     * 获取 AccessToken
     * @return java.lang.String
     * @Date 9:10 PM 11/13/2019
     **/
    public String getAccessToken() {
        long curTime = System.currentTimeMillis();
        if (accessToken == null || curTime - tokenTime >= cacheTime ) {
            DefaultDingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
            OapiGettokenRequest request = new OapiGettokenRequest();
            request.setAppkey(APP_KEY);
            request.setAppsecret(APP_SECRET);
            request.setHttpMethod("GET");
            try {
                OapiGettokenResponse response = client.execute(request);
                accessToken = response.getAccessToken();
                tokenTime = System.currentTimeMillis();
            } catch (ApiException e) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "DingtalkUtils 获取accesstoken失败");
            }
            log.debug("AccessToken 快要过期，重新获取");
        }
        return accessToken;
    }


    /**
     * 获取 Jsapi Ticket
     * @return java.lang.String
     * @Date 8:20 AM 2/23/2020
     **/
    public String getJsapiTicket()  {
        long curTime = System.currentTimeMillis();
        if (jsapiTicket == null || curTime - ticketTime >= cacheTime) {
            DefaultDingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/get_jsapi_ticket");
            OapiGetJsapiTicketRequest req = new OapiGetJsapiTicketRequest();
            req.setTopHttpMethod("GET");
            try {
                OapiGetJsapiTicketResponse response = client.execute(req, getAccessToken());
                jsapiTicket = response.getTicket();
                ticketTime = System.currentTimeMillis();
            } catch (ApiException e) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "DingtalkUtils 获取JsapiTicket失败");
            }
            log.debug("JsapiTicket 快要过期，重新获取");
        }
        return jsapiTicket;
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
            OapiUserGetuserinfoResponse response = client.execute(request, getAccessToken());
            userId = response.getUserid();
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return userId;
    }


    /**
     * 根据 userid 获取用户详细信息
     * @param userid
     * @return
     */
    public OapiUserGetResponse fetchUserDetail(String userid) {
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
        return response;
    }


    /**
     * 获取周报信息
     * @param userid
     * @param date
     * @return
     */
    public Map getReport(String userid, LocalDate date) {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/report/list");
        OapiReportListRequest request = new OapiReportListRequest();
        request.setUserid(userid);
        Long startTime = LocalDateTime.of(date, LocalTime.of(8,0)).toInstant(ZoneOffset.of("+8")).toEpochMilli();
        //开始时间
        request.setStartTime(startTime);
        //结束时间
        request.setEndTime(startTime + TimeUnit.DAYS.toMillis(5));
        request.setCursor(0L);
        request.setSize(1L);
        OapiReportListResponse response;
        try {
            response = client.execute(request, getAccessToken());
        } catch (ApiException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "获取周报失败");
        }
        if (response.getResult().getDataList().size() == 0) {
            // 无数据
            return Map.of();
        } else {
            List<OapiReportListResponse.JsonObject> contents = response.getResult().getDataList().get(0).getContents().stream()
                    .filter((item) -> !item.getValue().isEmpty())
                    .collect(Collectors.toList());
            return Map.of("contents", contents);
        }
    }


    /**
     * 获取部门id
     * @return
     */
    public List<String> listDepid() {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/department/list");
        OapiDepartmentListRequest request = new OapiDepartmentListRequest();
        request.setHttpMethod("GET");
        OapiDepartmentListResponse response;
        try {
            response = client.execute(request, getAccessToken());
        } catch (ApiException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "获取部门Id 失败");
        }
        return response.getDepartment().stream().map(x -> String.valueOf(x.getId())).collect(Collectors.toList());
    }


    /**
     * 查询所有部门信息
     * @return
     */
    public List<OapiDepartmentListResponse.Department> fetchDeptInfo() {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/department/list");
        OapiDepartmentListRequest request = new OapiDepartmentListRequest();
        request.setHttpMethod("GET");
        OapiDepartmentListResponse response;
        try {
            response = client.execute(request, getAccessToken());
        } catch (ApiException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "获取部门Id 失败");
        }
        return response.getDepartment();
    }


    /**
     * 获取整个部门的userid
     * @param depid
     * @return
     */
    public List<String> listUserId(String depid) {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/getDeptMember");
        OapiUserGetDeptMemberRequest req = new OapiUserGetDeptMemberRequest();
        req.setDeptId(depid);
        req.setHttpMethod("GET");
        OapiUserGetDeptMemberResponse response;
        try {
            response = client.execute(req, getAccessToken());
        } catch (ApiException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "获取getUserIds失败");
        }

        return response.getUserIds();
    }

    // 生成钉钉内部跳转链接

    /**
     * 生成跳转到投票页面的钉钉链接
     * @param isInternal 是否是内部论文评审投票
     * @param pid 对应的论文id
     * @return
     */
    private String createDingTalkLink(boolean isExternal, int pid) {
        StringBuffer curl = null;
        if (isExternal) {
            // 外部论文评审的链接
            curl = new StringBuffer().append("dingtalk://dingtalkclient/action/openapp?corpid=").append(CORPID)
                    .append("&container_type=work_platform&app_id=0_").append(AGENTID).append("&redirect_type=jump&redirect_url=")
                    .append(DOMAIN).append("/paper/ex-detail/").append(pid).append("/vote?type=external");
        } else {
            // 内部论文评审的链接
            curl = new StringBuffer().append("dingtalk://dingtalkclient/action/openapp?corpid=").append(CORPID)
                    .append("&container_type=work_platform&app_id=0_").append(AGENTID).append("&redirect_type=jump&redirect_url=")
                    .append(DOMAIN).append("/paper/detail/").append(pid).append("/vote?type=internal");
        }
        log.debug(curl.toString());
        return curl.toString();
    }

    /**
     * 发起投票时向群中发送消息
     * @param pid
     * @param title
     * @param endtime
     * @param namelist
     */
    public void sendVoteMsg(int pid, boolean isExternal, String title, String endtime, List<String> namelist) {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/chat/send");
        OapiChatSendRequest request = new OapiChatSendRequest();
        request.setChatid(CHAT_ID);
        OapiChatSendRequest.ActionCard actionCard = new OapiChatSendRequest.ActionCard();

        if (!isExternal) {
            // 如果是内部评审投票
            StringBuffer content = new StringBuffer().append(" #### 投票 \n ##### 论文： ").append(title).append(" \n ##### 作者： ");
            for (String name : namelist) {
                content.append(name).append(", ");
            }
            content.append(" \n 截止时间: ").append(endtime);
            actionCard.setTitle("内部评审投票");
            actionCard.setMarkdown(content.toString());
        } else {
            // 如果是外部评审投票
            StringBuffer content = new StringBuffer().append(" #### 投票 \n ##### 论文： ").append(title);
            content.append(" \n 截止时间: ").append(endtime);
            actionCard.setTitle("外部评审投票");
            actionCard.setMarkdown(content.toString());

        }

        actionCard.setSingleTitle("前往投票");
        actionCard.setSingleUrl(createDingTalkLink(isExternal, pid));

        request.setActionCard(actionCard);
        request.setMsgtype("action_card");

        try {
            OapiChatSendResponse response = client.execute(request, getAccessToken());
        } catch (ApiException e) {
            e.printStackTrace();
        }

    }


    /**
     * 发送投票结果
     * @param pid
     * @param title
     * @param result
     * @param accept
     * @param total
     */
    public void sendVoteResult(int pid, String title, boolean result, int accept, int total) {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/chat/send");
        OapiChatSendRequest request = new OapiChatSendRequest();
        request.setChatid(CHAT_ID);
        OapiChatSendRequest.ActionCard actionCard = new OapiChatSendRequest.ActionCard();

        StringBuffer content = new StringBuffer().append(" #### 投票结果 \n ##### 论文： ").append(title)
                .append(" \n 最终结果： ").append(result ? "Accept" : "reject")
                .append("  \n  Accept: ").append(accept).append(" 票  \n ")
                .append("Reject: ").append(total-accept).append(" 票  \n ")
                .append("已参与人数： ").append(total).append("人  \n ");

        //todo 内部评审和外部评审的投票地址不同

        StringBuffer pcurl = new StringBuffer().append("dingtalk://dingtalkclient/action/openapp?corpid=").append(CORPID)
                .append("&container_type=work_platform&app_id=0_").append(AGENTID).append("&redirect_type=jump&redirect_url=")
                .append(DOMAIN).append("/paper/detail/").append(pid).append("/vote");


        actionCard.setTitle("投票结果");
        actionCard.setMarkdown(content.toString());
        actionCard.setSingleTitle("查看详情");
        actionCard.setSingleUrl(pcurl.toString());


        request.setActionCard(actionCard);
        request.setMsgtype("action_card");

        try {
            OapiChatSendResponse response = client.execute(request, getAccessToken());

            log.debug(response.getBody());
        } catch (ApiException e) {
            e.printStackTrace();
        }
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



//    public void sentGroupMessage() {
//        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/chat/send");
//        OapiChatSendRequest request = new OapiChatSendRequest();
//        request.setChatid(CHAT_ID);
//        OapiChatSendRequest.ActionCard actionCard = new OapiChatSendRequest.ActionCard();
//
//        actionCard.setTitle("下午好，打扰了，这是一个测试标题");
//        actionCard.setMarkdown("markdown 内容，今天雨夹雪，雨夹雪，雨夹雪，\n  ###### 6号标题\n + 1 + 2 + 3，");
//        actionCard.setBtnOrientation("1");
//
//        OapiChatSendRequest.BtnJson btn1 = new OapiChatSendRequest.BtnJson();
//        btn1.setTitle("test");
//        btn1.setActionUrl("http://www.baidu.com");
//
//        OapiChatSendRequest.BtnJson btn2 = new OapiChatSendRequest.BtnJson();
//        btn2.setTitle("PC端");
//        btn2.setActionUrl("dingtalk://dingtalkclient/action/openapp?corpid=dingeff939842ad9207f35c2f4657eb6378f&container_type=work_platform&app_id=0_313704868&redirect_type=jump&redirect_url=http://www.dingdev.xyz:8080/paper/vote/2");
//
//        List<OapiChatSendRequest.BtnJson> btnJsonList = new ArrayList<>();
//
//        btnJsonList.add(btn1);
//        btnJsonList.add(btn2);
//
//        actionCard.setBtnJsonList(btnJsonList);
//
//        request.setActionCard(actionCard);
//        request.setMsgtype("action_card");
//
//        try {
//            OapiChatSendResponse response = client.execute(request, getAccessToken());
//        } catch (ApiException e) {
//            e.printStackTrace();
//        }
//    }



}

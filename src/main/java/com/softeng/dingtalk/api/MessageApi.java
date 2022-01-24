package com.softeng.dingtalk.api;

import com.dingtalk.api.request.OapiChatSendRequest;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.alibaba.fastjson.JSONObject;
import com.softeng.dingtalk.constant.DingApiUrlConstant;
import com.softeng.dingtalk.constant.ImageUrlConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @date: 2021/4/23 19:59
 */
@Slf4j
@Component
public class MessageApi extends BaseApi {

    /**
     * 跳转到钉钉微应用的url
     * @param url
     * @return
     */
    private String createLinkRedirectToApp(String url) {
        StringBuffer curl = new StringBuffer().append("dingtalk://dingtalkclient/action/openapp?corpid=").append(CORPID)
                .append("&container_type=work_platform&app_id=0_").append(AGENTID).append("&redirect_type=jump&redirect_url=")
                .append(DOMAIN).append(url);
        log.debug(curl.toString());
        return curl.toString();
    }

    /**
     * 创建一个消息卡片，用于设置到请求体中
     * @param title
     * @param singleTitle
     * @param markdown
     * @param url
     * @return
     */
    private OapiChatSendRequest.ActionCard createActionCard(String title, String markdown, String singleTitle, String singleUrl) {
        OapiChatSendRequest.ActionCard actionCard = new OapiChatSendRequest.ActionCard();
        actionCard.setTitle(title);
        actionCard.setMarkdown(markdown);
        actionCard.setSingleTitle(singleTitle);
        actionCard.setSingleUrl(singleUrl);
        return actionCard;
    }

    /**
     * 向钉钉群中发送消息卡片
     * @param title
     * @param singleTitle
     * @param markdown
     * @param url
     */
    public void sendActionCard(String title, String markdown, String singleTitle, String url) {
        OapiChatSendRequest request = new OapiChatSendRequest();
        request.setChatid(CHAT_ID);
        request.setActionCard(createActionCard(title, markdown, singleTitle, createLinkRedirectToApp(url)));
        request.setMsgtype("action_card");

        executeRequest(request, "https://oapi.dingtalk.com/chat/send");
    }

    /**
     * 向指定的人以本应用的名义发送连接消息
     */
    public void sendLinkMessage(String title, String linkUrl, String text, List<String> users) {
        if(users.size() > 100) {
            throw new RuntimeException("群发消息一次不能超过一百人(钉钉接口文档注明)");
        }

        var link = new OapiMessageCorpconversationAsyncsendV2Request.Link();
        link.setMessageUrl(createLinkRedirectToApp(linkUrl));
        link.setPicUrl(ImageUrlConstant.SYSTEM_IMAGE_URL);
        link.setText(text);
        link.setTitle(title);

        var msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
        msg.setMsgtype("link");
        msg.setLink(link);

        OapiMessageCorpconversationAsyncsendV2Request req = new OapiMessageCorpconversationAsyncsendV2Request();
        req.setAgentId(AGENTID);
        req.setUseridList(String.join(",", users));
        req.setMsg(msg);

        var resp = executeRequest(req, DingApiUrlConstant.LINK_MESSAGE_API_URL);
        log.info("send link message response: {}", JSONObject.toJSON(resp));

    }


}


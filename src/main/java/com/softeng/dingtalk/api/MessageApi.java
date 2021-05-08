package com.softeng.dingtalk.api;

import com.dingtalk.api.request.OapiChatSendRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
}

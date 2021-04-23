package com.softeng.pms.dingtalk;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiChatSendRequest;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.taobao.api.ApiException;
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

    public void sendActionCard(String title, String markdown, String singleTitle, String url) {
        OapiChatSendRequest request = new OapiChatSendRequest();
        request.setChatid(CHAT_ID);
        request.setActionCard(createActionCard(title, markdown, singleTitle, createLinkRedirectToApp(url)));
        request.setMsgtype("action_card");

        executeRequest(request, "https://oapi.dingtalk.com/chat/send");
    }

    public void sendWorkMessage(List<String> userids, String title, String markdown, String singleTitle, String url) {
        OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
        request.setAgentId(AGENTID);
        request.setUseridList("306147243334957616");
        request.setToAllUser(true);

        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();

        msg.setOa(new OapiMessageCorpconversationAsyncsendV2Request.OA());
        msg.getOa().setHead(new OapiMessageCorpconversationAsyncsendV2Request.Head());
        msg.getOa().getHead().setText("head");
        msg.getOa().setBody(new OapiMessageCorpconversationAsyncsendV2Request.Body());
        msg.getOa().getBody().setContent("xxx");
        msg.setMsgtype("oa");
        request.setMsg(msg);

        msg.setActionCard(new OapiMessageCorpconversationAsyncsendV2Request.ActionCard());
        msg.getActionCard().setTitle("xxx123411111");
        msg.getActionCard().setMarkdown("### 测试123111");
        msg.getActionCard().setSingleTitle("测试测试");
        msg.getActionCard().setSingleUrl("https://www.dingtalk.com");
        msg.setMsgtype("action_card");
        request.setMsg(msg);

        OapiMessageCorpconversationAsyncsendV2Response rsp = executeRequest(request, "https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2");
        log.debug(rsp.getBody());

    }
}

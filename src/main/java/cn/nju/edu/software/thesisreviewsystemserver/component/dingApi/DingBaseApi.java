package cn.nju.edu.software.thesisreviewsystemserver.component.dingApi;

import com.aliyun.dingtalkoauth2_1_0.Client;
import com.aliyun.tea.*;
import com.aliyun.teautil.*;
import com.aliyun.teaopenapi.models.*;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiUserGetuserinfoRequest;
import com.dingtalk.api.response.OapiUserGetuserinfoResponse;
import com.github.benmanes.caffeine.cache.Cache;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

@Slf4j
public class DingBaseApi {

    protected String corpId;        // 钉钉组织的唯一id

    protected String appKey;       // 钉钉微应用的 key

    protected String appSecret;    // 钉钉微应用的密钥

    @Resource
    Cache<String, String> cache;

    /**
     * 使用 Token 初始化账号Client
     * @return Client
     * @throws Exception
     */
    public Client createClient() throws Exception {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        return new Client(config);
    }

    public String getAccessToken() throws Exception {
        String res = cache.asMap().get("AccessToken");
        if(res == null) {
            Client client = this.createClient();
            com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenRequest getAccessTokenRequest = new com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenRequest()
                    .setAppKey(appKey)
                    .setAppSecret(appSecret);
            try {
                res = client.getAccessToken(getAccessTokenRequest).body.getAccessToken();
            } catch (TeaException err) {
                if (!Common.empty(err.code) && !Common.empty(err.message)) {
                    // err 中含有 code 和 message 属性，可帮助开发定位问题
                    log.error(err.code + " " + err.message);
                }
            } catch (Exception _err) {
                TeaException err = new TeaException(_err.getMessage(), _err);
                if (!Common.empty(err.code) && !Common.empty(err.message)) {
                    // err 中含有 code 和 message 属性，可帮助开发定位问题
                    log.error(err.code + " " + err.message);
                }
            }
        }
        return res;
    }

    public String getUserId(String requestAuthCode) throws Exception {
        DingTalkClient client = new DefaultDingTalkClient(DingApiUrlEnum.GET_USER_INFO.getUrl());
        OapiUserGetuserinfoRequest request = new OapiUserGetuserinfoRequest();
        request.setCode(requestAuthCode);
        request.setHttpMethod("GET");
        OapiUserGetuserinfoResponse response;
        try {
            response = client.execute(request, this.getAccessToken());
        } catch (ApiException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return response.getUserid();
    }
}

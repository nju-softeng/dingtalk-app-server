package cn.nju.edu.software.thesisreviewsystemserver.component.dingApi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DingExpertGroupApi extends DingBaseApi{
    @Value("${expertGroup.corpId}")
    public void setCorID(String corpId) {
        this.corpId = corpId;
    }

    @Value("${expertGroup.app_key}")
    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    @Value("${expertGroup.app_secret}")
    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }
}

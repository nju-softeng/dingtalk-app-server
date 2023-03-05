package cn.nju.edu.software.thesisreviewsystemserver.component.dingApi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DingStudentGroupApi extends DingExpertGroupApi{
    @Value("${studentGroup.corpId}")
    public void setCorID(String corpId) {
        this.corpId = corpId;
    }

    @Value("${studentGroup.app_key}")
    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    @Value("${studentGroup.app_secret}")
    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }
}

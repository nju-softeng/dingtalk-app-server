package com.softeng.dingtalk;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DingtalkApplicationTests {

    @Test
    void contextLoads() {
        DefaultDingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
        OapiGettokenRequest request = new OapiGettokenRequest();
        request.setAppkey("dingk9nmede0wzi7aywt");
        request.setAppsecret("nh3mY7PPMAne3aDEpyANGjKlQIFLBkPQ0npYUnOELNVSJFuKST-ngsrfMK2sZiB9");
        request.setHttpMethod("GET");
        try {
            OapiGettokenResponse response = client.execute(request);
            System.out.println(response.getAccessToken());
        } catch (Exception e) {

        }
    }

}

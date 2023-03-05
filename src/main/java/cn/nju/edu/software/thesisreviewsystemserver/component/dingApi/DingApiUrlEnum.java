package cn.nju.edu.software.thesisreviewsystemserver.component.dingApi;

import lombok.Getter;

@Getter
public enum DingApiUrlEnum {
    GET_USER_INFO("https://oapi.dingtalk.com/user/getuserinfo");

    private String url;
    DingApiUrlEnum(String url) {
        this.url = url;
    }
}

package com.softeng.dingtalk.api;

import org.springframework.stereotype.Component;

/**
 * @date: 2021/4/23 17:15
 */
@Component
public class TokenCache {
    /**
     * 缓存有效时间 1小时 50分钟
     */
    public final long cacheTime = 1000 * 60 * 55 * 2;

    /**
     * 缓存的accessToken: 不可直接调用，以防过期
     */
    public String accessToken;

    /**
     * 缓存时间
     */
    public long tokenTime;

    /**
     * 缓存的accessToken jsapi_ticket: 不可直接调用，以防过期
     */
    public String jsapiTicket;

    /**
     * 缓存时间
     */
    public long ticketTime;
}

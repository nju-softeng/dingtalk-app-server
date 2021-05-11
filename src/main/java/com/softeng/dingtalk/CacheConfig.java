package com.softeng.dingtalk;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {
    /**
     * Caffeine 缓存
     * @return
     */
    @Bean
    public Cache<String, String> caffeineCache() {
        return Caffeine.newBuilder().expireAfterWrite(60 * 55 * 2, TimeUnit.SECONDS)
                .maximumSize(100).build();
    }
}
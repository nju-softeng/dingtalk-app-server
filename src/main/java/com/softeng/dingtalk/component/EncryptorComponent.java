package com.softeng.dingtalk.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

/**
 * @author zhanyeye
 * @description 加密和解密的组件，用于token的加密和解密
 * @create 12/11/2019 10:08 AM
 */
@Component
public class EncryptorComponent {
    @Value("${my.secretkey}")
    private String ENCRYPT_SECRET_KEY;
    @Value("${my.salt}")
    private String FIXED_SALT;

    public static final int DYNAMIC_SALT_LENGTH = 10;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 加密: 将一个map序列化为字符串，并加密
     * @param payload
     * @return
     */
    public String encrypt(Map payload) {
        try {
            String json = objectMapper.writeValueAsString(payload);
            return Encryptors.text(ENCRYPT_SECRET_KEY, FIXED_SALT).encrypt(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解密: 将字符串反序列化为map, 并解密
     * @param encryptString
     * @return
     */
    public Map<String, Object> decrypt(String encryptString) {
        try {
            String json = Encryptors.text(ENCRYPT_SECRET_KEY, FIXED_SALT).decrypt(encryptString);
            return objectMapper.readValue(json, Map.class);
        } catch (Exception e) {
            //若反序列化时抛异常，则说明 token 是伪造的，未登录！
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "未登录");
        }
    }


    /**
     * @description 用于ContextHolder的加解密
     */

    public <T> String encrypt(T obj) {
        try {
            String json = new ObjectMapper().writeValueAsString(obj);
            String dynamicSalt = RandomStringUtils.randomAlphabetic(DYNAMIC_SALT_LENGTH);
            return Encryptors
                    .text(ENCRYPT_SECRET_KEY, FIXED_SALT)
                    .encrypt(dynamicSalt + json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T decrypt(String str, Class<T> tClass) {
        try {
            String plainText = Encryptors.text(ENCRYPT_SECRET_KEY, FIXED_SALT).decrypt(str);
            return new ObjectMapper().readValue(plainText.substring(DYNAMIC_SALT_LENGTH), tClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

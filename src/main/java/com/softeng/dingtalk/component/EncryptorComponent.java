package com.softeng.dingtalk.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private String secretKey;
    @Value("${my.salt}")
    private String salt;

    @Autowired
    private ObjectMapper objectMapper;

    //加密
    public String encrypt(Map payload) {
        try {
            String json = objectMapper.writeValueAsString(payload);
            return Encryptors.text(secretKey, salt).encrypt(json);
        } catch (JsonProcessingException e) {

        }
        return null;
    }


    //解密
    public Map<String, Object> decrypt(String encryptString) {
        try {
            String json = Encryptors.text(secretKey, salt).decrypt(encryptString);
            return objectMapper.readValue(json, Map.class);
        } catch (Exception e) {
            //若反序列化时抛异常，则说明 token 是伪造的，未登录！
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "未登录");
        }
    }
}

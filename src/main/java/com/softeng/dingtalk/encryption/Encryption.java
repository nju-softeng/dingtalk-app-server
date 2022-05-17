package com.softeng.dingtalk.encryption;

import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Encryption {

    @Value("${encryption.key}")
    private  String key;

    /**
     * 加密
     * @param words
     * @return
     */
    public String doEncrypt( String words) {
        if(words==null) return null;
        BasicTextEncryptor encryptor = new BasicTextEncryptor();
        encryptor.setPassword(key);
        return encryptor.encrypt(words);
    }

    /**
     * 解密
     * @param encryptWords
     * @return
     */
    public String doDecrypt( String encryptWords) {
        if(encryptWords==null) return null;
        BasicTextEncryptor encryptor = new BasicTextEncryptor();
        encryptor.setPassword(key);
        return encryptor.decrypt(encryptWords);
    }


}

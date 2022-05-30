package com.softeng.dingtalk.fabric;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.hyperledger.fabric.sdk.Enrollment;
import org.springframework.stereotype.Component;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Description 存储配置对象
 * @Author Jerrian Zhao
 * @Data 03/04/2022
 */
public class FabricStore {
    private String file;
    private final Map<String, FabricUser> members = new HashMap<>();

    public FabricStore(File file) {
        this.file = file.getAbsolutePath();
    }

    /**
     * 加载属性
     *
     * @return
     */
    private Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(file)) {
            properties.load(input);
        } catch (FileNotFoundException e) {
            System.out.println(String.format("Found no \"%s\"", file));
        } catch (IOException e) {
            System.out.println(String.format("Load Store from \"%s\" failed, reason:%s", file, e.getMessage()));
        }
        return properties;
    }

    /**
     * 设置名称-值
     *
     * @param name
     * @param value
     */
    public void setValue(String name, String value) {
        Properties properties = loadProperties();
        try (OutputStream output = new FileOutputStream(file)) {
            properties.setProperty(name, value);
            properties.store(output, "");
        } catch (IOException e) {
            System.out.println(String.format("Save the Store failed, reason:%s", e.getMessage()));
        }
    }

    public String getValue(String name) {
        Properties properties = loadProperties();
        return properties.getProperty(name);
    }

    private PrivateKey getPrivateKeyFromBytes(byte[] data) throws IOException, NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException {
        final Reader pemReader = new StringReader(new String(data));
        final PrivateKeyInfo pemPair;
        try (PEMParser pemParser = new PEMParser(pemReader)) {
            pemPair = (PrivateKeyInfo) pemParser.readObject();
        }
        PrivateKey privateKey = new JcaPEMKeyConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME).getPrivateKey(pemPair);
        return privateKey;
    }

    static final class StoreEnrollement implements Enrollment, Serializable {
        private static final long serialVersionUID = -7460795537417557178L;
        private final PrivateKey privateKey; //私钥
        private final String certificate; //证书

        StoreEnrollement(PrivateKey privateKey, String certificate) {
            this.certificate = certificate;
            this.privateKey = privateKey;
        }

        @Override
        public PrivateKey getKey() {
            return privateKey;
        }

        @Override
        public String getCert() {
            return certificate;
        }
    }

    public FabricUser getMember(String name, String organization) {
        FabricUser fabricUser = members.get(FabricUser.transferStoreName(name, organization));
        if (fabricUser != null) {
            return fabricUser;
        }
        fabricUser = new FabricUser(name, organization, this); //如果没有找到则尝试恢复
        return fabricUser;
    }

    public FabricUser getMember(String name, String organization, String mspId, File privateKeyFile, File certificateFile) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {
        try {
            FabricUser fabricUser = members.get(FabricUser.transferStoreName(name, organization));
            if (fabricUser != null) {
                return fabricUser;
            }
            fabricUser = new FabricUser(name, organization, this); //如果没有找到则尝试恢复
            fabricUser.setMspId(mspId);
            String certificate = new String(IOUtils.toByteArray(new FileInputStream(certificateFile)), "UTF-8");
            PrivateKey privateKey = getPrivateKeyFromBytes(IOUtils.toByteArray(new FileInputStream(privateKeyFile)));
            fabricUser.setEnrollment(new StoreEnrollement(privateKey, certificate));
            return fabricUser;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    static {
        try {
            Security.addProvider(new BouncyCastleProvider());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.softeng.dingtalk.fabric;

import io.netty.util.internal.StringUtil;
import org.bouncycastle.util.encoders.Hex;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;

import java.io.*;
import java.util.Set;

/**
 * @Description 联盟用户对象
 * @Author Jerrian Zhao
 * @Data 03/03/2022
 */
public class FabricUser implements User, Serializable {
    private static final long serialVersionUID = -2490138626124133924L;
    private String name; //名字
    private Set<String> roles; // 角色
    private String account; //账号
    private String affiliation; //所属联盟
    private String organization; //组织
    private String mspId; // 会员Id
    Enrollment enrollment = null; //注册操作
    private String enrollmentSecret; //注册操作密码

    private transient FabricStore fabricStore; //存储对象
    private String fabricStoreName; //存储对象名

    public FabricUser(String name, String organization, FabricStore fabricStore) {
        this.name = name;
        this.organization = organization;
        this.fabricStore = fabricStore;
        this.fabricStoreName = transferStoreName(name, organization);

        String member = fabricStore.getValue(fabricStoreName);
        if (member != null) {
            save();
        } else {
            restore();
        }
    }

    public static String transferStoreName(String name, String org) {
        System.out.println("toKeyValStoreName = " + "user." + name + org);
        return "user." + name + org;
    }

    /**
     * 存储配置对象
     */
    public void save() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            oos.flush();
            fabricStore.setValue(fabricStoreName, Hex.toHexString(baos.toByteArray()));
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从Hex键值存储中恢复
     * 如果找到则赋值恢复状态，如果找不到则返回null
     *
     * @return
     */
    private FabricUser restore() {
        String member = fabricStore.getValue(fabricStoreName);
        if (member != null) {
            byte[] serialized = Hex.decode(member);
            ByteArrayInputStream bais = new ByteArrayInputStream(serialized);
            try {
                ObjectInputStream ois = new ObjectInputStream(bais);
                FabricUser fabricUser = (FabricUser) ois.readObject();
                if (fabricUser != null) {
                    this.name = fabricUser.name;
                    this.roles = fabricUser.roles;
                    this.account = fabricUser.account;
                    this.affiliation = fabricUser.affiliation;
                    this.organization = fabricUser.organization;
                    this.mspId = fabricUser.mspId;
                    this.enrollment = fabricUser.enrollment;
                    this.enrollmentSecret = fabricUser.enrollmentSecret;
                    return this;
                }
            } catch (Exception e) {
                throw new RuntimeException(String.format("Restore state of member %s failed", this.name), e);
            }
        }
        return null;
    }

    @Override
    public String getName() {
        return this.name;
    }

    /**
     * 设置角色并更新存储配置对象
     *
     * @param roles
     */
    public void setRoles(Set<String> roles) {
        this.roles = roles;
        save();
    }

    @Override
    public Set<String> getRoles() {
        return this.roles;
    }

    @Override
    public String getAccount() {
        return this.account;
    }

    /**
     * 设置账号并更新存储配置对象
     *
     * @param account
     */
    public void setAccount(String account) {
        this.account = account;
        save();
    }

    /**
     * 设置联盟并更新存储配置对象
     *
     * @param affiliation
     */
    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
        save();
    }

    @Override
    public String getAffiliation() {
        return this.affiliation;
    }

    /**
     * 设置会员Id并更新存储配置对象
     *
     * @param mspId
     */
    public void setMspId(String mspId) {
        this.mspId = mspId;
        save();
    }

    @Override
    public String getMspId() {
        return this.mspId;
    }

    @Override
    public Enrollment getEnrollment() {
        return this.enrollment;
    }

    /**
     * 设置注册操作并更新存储配置对象
     *
     * @param enrollment
     */
    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
        save();
    }

    /**
     * 设置注册操作密码并更新存储配置对象
     *
     * @param enrollmentSecret
     */
    public void setEnrollmentSecret(String enrollmentSecret) {
        this.enrollmentSecret = enrollmentSecret;
        save();
    }

    /**
     * 检查该用户是否已注册(register)
     *
     * @return
     */
    public boolean isRegistered() {
        return !StringUtil.isNullOrEmpty(enrollmentSecret);
    }

    /**
     * 检查该用户是否已被登记(enroll)
     *
     * @return
     */
    public boolean isEnrolled() {
        return this.enrollment != null;
    }
}

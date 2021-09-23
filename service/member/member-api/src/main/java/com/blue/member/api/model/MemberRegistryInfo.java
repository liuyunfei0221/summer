package com.blue.member.api.model;

import java.io.Serializable;

/**
 * 成员注册信息封装
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class MemberRegistryInfo implements Serializable {

    private static final long serialVersionUID = 8543617230220651524L;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 昵称
     */
    private String name;

    /**
     * 图标url
     */
    private String icon;

    /**
     * 性别 1男 0女 2其他
     */
    private Integer gender;

    public MemberRegistryInfo() {
    }

    public MemberRegistryInfo(String phone, String email, String password, String name, String icon, Integer gender) {
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.name = name;
        this.icon = icon;
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "MemberRegistryInfo{" +
                "phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", gender=" + gender +
                '}';
    }

}

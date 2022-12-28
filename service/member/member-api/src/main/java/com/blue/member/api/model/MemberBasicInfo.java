package com.blue.member.api.model;

import com.blue.basic.serializer.Long2StringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * member basic info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class MemberBasicInfo implements Serializable {

    private static final long serialVersionUID = -8231116867917923473L;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long id;

    private String phone;

    private String email;

    private String name;

    private String icon;

    private String qrCode;

    /**
     * @see com.blue.basic.constant.member.Gender
     */
    private Integer gender;

    private String introduction;

    /**
     * @see com.blue.basic.constant.common.Status
     */
    private Integer status;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long createTime;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long updateTime;

    public MemberBasicInfo() {
    }

    public MemberBasicInfo(Long id, String phone, String email, String name, String icon, String qrCode, Integer gender, String introduction, Integer status, Long createTime, Long updateTime) {
        this.id = id;
        this.phone = phone;
        this.email = email;
        this.name = name;
        this.icon = icon;
        this.qrCode = qrCode;
        this.gender = gender;
        this.introduction = introduction;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "MemberBasicInfo{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", qrCode='" + qrCode + '\'' +
                ", gender=" + gender +
                ", introduction='" + introduction + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

}

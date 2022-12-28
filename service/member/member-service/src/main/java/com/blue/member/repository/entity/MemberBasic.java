package com.blue.member.repository.entity;

import java.io.Serializable;

/**
 * member basic entity
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class MemberBasic implements Serializable {

    private static final long serialVersionUID = 6361053486435728060L;

    private Long id;

    /**
     * phone format: 8613131693996
     */
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

    private String source;

    /**
     * @see com.blue.basic.constant.common.Status
     */
    private Integer status;

    private Long createTime;

    private Long updateTime;


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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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
        return "MemberBasic{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", qrCode='" + qrCode + '\'' +
                ", gender=" + gender +
                ", introduction='" + introduction + '\'' +
                ", source='" + source + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

}
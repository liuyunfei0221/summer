package com.blue.member.api.model;

import java.io.Serializable;

/**
 * 成员基础信息表
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class MemberBasicInfo implements Serializable {

    private static final long serialVersionUID = -8231116867917923473L;

    /**
     * 主键
     */
    private Long id;

    //邮箱

    /**
     * 登录密码
     */
    private String password;

    /**
     * 手机号
     */
    private String phone;

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

    /**
     * 状态，1可用 0禁用
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 修改时间
     */
    private Long updateTime;

    public MemberBasicInfo(Long id, String password, String phone, String name, String icon,
                           Integer gender, Integer status, Long createTime, Long updateTime) {
        this.id = id;
        this.password = password;
        this.phone = phone;
        this.name = name;
        this.icon = icon;
        this.gender = gender;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
        return "MemberBasicDTO{" +
                "id=" + id +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", gender=" + gender +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

}

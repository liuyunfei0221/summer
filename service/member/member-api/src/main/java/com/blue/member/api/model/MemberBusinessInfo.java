package com.blue.member.api.model;

import java.io.Serializable;


/**
 * member business info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class MemberBusinessInfo implements Serializable {

    private static final long serialVersionUID = -8143224829179932143L;

    private Long id;

    private Long memberId;

    private String qrCode;

    private String profile;

    private String extra;

    private Integer status;

    private Long createTime;

    private Long updateTime;

    public MemberBusinessInfo() {
    }

    public MemberBusinessInfo(Long id, Long memberId, String qrCode, String profile, String extra, Integer status, Long createTime, Long updateTime) {
        this.id = id;
        this.memberId = memberId;
        this.qrCode = qrCode;
        this.profile = profile;
        this.extra = extra;
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

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
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
        return "MemberBusinessInfo{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", qrCode='" + qrCode + '\'' +
                ", profile='" + profile + '\'' +
                ", extra='" + extra + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

}

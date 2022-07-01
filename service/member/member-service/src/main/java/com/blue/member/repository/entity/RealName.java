package com.blue.member.repository.entity;

import java.io.Serializable;

import static com.blue.base.common.base.BlueChecker.isNull;

/**
 * member real name entity
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class RealName implements Serializable {

    private static final long serialVersionUID = -2653604103780424227L;

    private Long id;

    private Long memberId;

    private String realName;

    private Integer gender;

    private String birthday;

    private String nationality;

    private String ethnic;

    private String idCardNo;

    private String residenceAddress;

    private String issuingAuthority;

    private String sinceDate;

    private String expireDate;

    private String extra;

    private Integer status;

    private Long createTime;

    private Long updateTime;

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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = isNull(realName) ? null : realName.trim();
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = isNull(birthday) ? null : birthday.trim();
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getEthnic() {
        return ethnic;
    }

    public void setEthnic(String ethnic) {
        this.ethnic = ethnic;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = isNull(idCardNo) ? null : idCardNo.trim();
    }

    public String getResidenceAddress() {
        return residenceAddress;
    }

    public void setResidenceAddress(String residenceAddress) {
        this.residenceAddress = isNull(residenceAddress) ? null : residenceAddress.trim();
    }

    public String getIssuingAuthority() {
        return issuingAuthority;
    }

    public void setIssuingAuthority(String issuingAuthority) {
        this.issuingAuthority = isNull(issuingAuthority) ? null : issuingAuthority.trim();
    }

    public String getSinceDate() {
        return sinceDate;
    }

    public void setSinceDate(String sinceDate) {
        this.sinceDate = isNull(sinceDate) ? null : sinceDate.trim();
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = isNull(expireDate) ? null : expireDate.trim();
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = isNull(extra) ? null : extra.trim();
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
        return "RealName{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", realName='" + realName + '\'' +
                ", gender=" + gender +
                ", birthday='" + birthday + '\'' +
                ", nationality=" + nationality +
                ", ethnic=" + ethnic +
                ", idCardNo='" + idCardNo + '\'' +
                ", residenceAddress='" + residenceAddress + '\'' +
                ", issuingAuthority='" + issuingAuthority + '\'' +
                ", sinceDate='" + sinceDate + '\'' +
                ", expireDate='" + expireDate + '\'' +
                ", extra='" + extra + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

}
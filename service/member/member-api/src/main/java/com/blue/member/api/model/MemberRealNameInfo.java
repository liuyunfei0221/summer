package com.blue.member.api.model;

import java.io.Serializable;


/**
 * member real name info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class MemberRealNameInfo implements Serializable {

    private static final long serialVersionUID = -4709215206709937940L;

    private Long id;

    private Long memberId;

    private String realName;

    private Integer gender;

    private String birthday;

    private Long nationalityId;

    private Long ethnicId;

    private String idCardNo;

    private String residenceAddress;

    private String issuingAuthority;

    private String sinceDate;

    private String expireDate;

    private String extra;

    private Integer status;

    public MemberRealNameInfo() {
    }

    public MemberRealNameInfo(Long id, Long memberId, String realName, Integer gender, String birthday,
                              Long nationalityId, Long ethnicId, String idCardNo, String residenceAddress,
                              String issuingAuthority, String sinceDate, String expireDate, String extra, Integer status) {
        this.id = id;
        this.memberId = memberId;
        this.realName = realName;
        this.gender = gender;
        this.birthday = birthday;
        this.nationalityId = nationalityId;
        this.ethnicId = ethnicId;
        this.idCardNo = idCardNo;
        this.residenceAddress = residenceAddress;
        this.issuingAuthority = issuingAuthority;
        this.sinceDate = sinceDate;
        this.expireDate = expireDate;
        this.extra = extra;
        this.status = status;
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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
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
        this.birthday = birthday;
    }

    public Long getNationalityId() {
        return nationalityId;
    }

    public void setNationalityId(Long nationalityId) {
        this.nationalityId = nationalityId;
    }

    public Long getEthnicId() {
        return ethnicId;
    }

    public void setEthnicId(Long ethnicId) {
        this.ethnicId = ethnicId;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getResidenceAddress() {
        return residenceAddress;
    }

    public void setResidenceAddress(String residenceAddress) {
        this.residenceAddress = residenceAddress;
    }

    public String getIssuingAuthority() {
        return issuingAuthority;
    }

    public void setIssuingAuthority(String issuingAuthority) {
        this.issuingAuthority = issuingAuthority;
    }

    public String getSinceDate() {
        return sinceDate;
    }

    public void setSinceDate(String sinceDate) {
        this.sinceDate = sinceDate;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
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

    @Override
    public String toString() {
        return "MemberRealNameInfo{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", realName='" + realName + '\'' +
                ", gender=" + gender +
                ", birthday='" + birthday + '\'' +
                ", nationalityId=" + nationalityId +
                ", ethnicId=" + ethnicId +
                ", idCardNo='" + idCardNo + '\'' +
                ", residenceAddress='" + residenceAddress + '\'' +
                ", issuingAuthority='" + issuingAuthority + '\'' +
                ", sinceDate='" + sinceDate + '\'' +
                ", expireDate='" + expireDate + '\'' +
                ", extra='" + extra + '\'' +
                ", status=" + status +
                '}';
    }

}

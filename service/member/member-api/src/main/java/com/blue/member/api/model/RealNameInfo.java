package com.blue.member.api.model;

import com.blue.basic.serializer.IdentityDeserializer;
import com.blue.basic.serializer.IdentitySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;


/**
 * real name info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class RealNameInfo implements Serializable {

    private static final long serialVersionUID = -4709215206709937940L;

    @JsonSerialize(using = IdentitySerializer.class)
    @JsonDeserialize(using = IdentityDeserializer.class)
    private Long id;

    @JsonSerialize(using = IdentitySerializer.class)
    @JsonDeserialize(using = IdentityDeserializer.class)
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

    public RealNameInfo() {
    }

    public RealNameInfo(Long id, Long memberId, String realName, Integer gender, String birthday,
                        String nationality, String ethnic, String idCardNo, String residenceAddress,
                        String issuingAuthority, String sinceDate, String expireDate, String extra, Integer status) {
        this.id = id;
        this.memberId = memberId;
        this.realName = realName;
        this.gender = gender;
        this.birthday = birthday;
        this.nationality = nationality;
        this.ethnic = ethnic;
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
                ", nationality=" + nationality +
                ", ethnic=" + ethnic +
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

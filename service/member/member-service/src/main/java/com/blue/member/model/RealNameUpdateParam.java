package com.blue.member.model;

import com.blue.base.inter.Asserter;
import com.blue.base.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.common.base.ConstantProcessor.assertGenderIdentity;
import static com.blue.base.constant.common.ResponseElement.BAD_REQUEST;

/**
 * params for update exist real name
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public class RealNameUpdateParam implements Serializable, Asserter {

    private static final long serialVersionUID = 4028727042786997123L;

    private String realName;

    private Integer gender;

    private String birthday;

    private Long nationalityId;

    private String ethnic;

    private String idCardNo;

    private String residenceAddress;

    private String issuingAuthority;

    private String sinceDate;

    private String expireDate;

    private String extra;

    public RealNameUpdateParam() {
    }

    public RealNameUpdateParam(String realName, Integer gender, String birthday, Long nationalityId, String ethnic, String idCardNo,
                               String residenceAddress, String issuingAuthority, String sinceDate, String expireDate, String extra) {
        this.realName = realName;
        this.gender = gender;
        this.birthday = birthday;
        this.nationalityId = nationalityId;
        this.ethnic = ethnic;
        this.idCardNo = idCardNo;
        this.residenceAddress = residenceAddress;
        this.issuingAuthority = issuingAuthority;
        this.sinceDate = sinceDate;
        this.expireDate = expireDate;
        this.extra = extra;
    }

    @Override
    public void asserts() {
        if (isBlank(this.realName))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "realName can't be blank");
        assertGenderIdentity(this.gender, false);
        if (isBlank(this.birthday))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "birthday can't be blank");
        if (isInvalidIdentity(nationalityId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid nationalityId");
        if (isBlank(this.ethnic))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "ethnic can't be blank");
        if (isBlank(this.idCardNo))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "idCardNo can't be blank");
        if (isBlank(this.residenceAddress))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "residenceAddress can't be blank");
        if (isBlank(this.issuingAuthority))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "issuingAuthority can't be blank");
        if (isBlank(this.sinceDate))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "sinceDate can't be blank");
        if (isBlank(this.expireDate))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "expireDate can't be blank");
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

    @Override
    public String toString() {
        return "RealNameUpdateParam{" +
                "realName='" + realName + '\'' +
                ", gender=" + gender +
                ", birthday='" + birthday + '\'' +
                ", nationalityId=" + nationalityId +
                ", ethnic='" + ethnic + '\'' +
                ", idCardNo='" + idCardNo + '\'' +
                ", residenceAddress='" + residenceAddress + '\'' +
                ", issuingAuthority='" + issuingAuthority + '\'' +
                ", sinceDate='" + sinceDate + '\'' +
                ", expireDate='" + expireDate + '\'' +
                ", extra='" + extra + '\'' +
                '}';
    }

}

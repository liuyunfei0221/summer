package com.blue.member.model;

import com.blue.basic.inter.Asserter;
import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isBlank;
import static com.blue.basic.common.base.ConstantProcessor.assertGender;
import static com.blue.basic.constant.common.ResponseElement.INVALID_PARAM;

/**
 * params for update exist real name
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class RealNameUpdateParam implements Serializable, Asserter {

    private static final long serialVersionUID = 4028727042786997123L;

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

    public RealNameUpdateParam() {
    }

    public RealNameUpdateParam(String realName, Integer gender, String birthday, String nationality, String ethnic, String idCardNo,
                               String residenceAddress, String issuingAuthority, String sinceDate, String expireDate, String extra) {
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
    }

    @Override
    public void asserts() {
        if (isBlank(this.realName))
            throw new BlueException(INVALID_PARAM);
        assertGender(this.gender, false);

        if (isBlank(this.birthday))
            throw new BlueException(INVALID_PARAM);
        if (isBlank(nationality))
            throw new BlueException(INVALID_PARAM);
        if (isBlank(this.ethnic))
            throw new BlueException(INVALID_PARAM);
        if (isBlank(this.idCardNo))
            throw new BlueException(INVALID_PARAM);
        if (isBlank(this.residenceAddress))
            throw new BlueException(INVALID_PARAM);
        if (isBlank(this.issuingAuthority))
            throw new BlueException(INVALID_PARAM);
        if (isBlank(this.sinceDate))
            throw new BlueException(INVALID_PARAM);
        if (isBlank(this.expireDate))
            throw new BlueException(INVALID_PARAM);
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

    @Override
    public String toString() {
        return "RealNameUpdateParam{" +
                "realName='" + realName + '\'' +
                ", gender=" + gender +
                ", birthday='" + birthday + '\'' +
                ", nationality=" + nationality +
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

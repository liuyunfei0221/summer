package com.blue.member.model;

import com.blue.base.inter.Asserter;
import com.blue.base.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.base.common.base.BlueChecker.isBlank;
import static com.blue.base.common.base.BlueChecker.isNull;
import static com.blue.base.common.base.ConstantProcessor.assertGenderIdentity;
import static com.blue.base.constant.common.ResponseElement.BAD_REQUEST;

/**
 * params for insert a new address
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public class AddressInsertParam implements Serializable, Asserter {

    private static final long serialVersionUID = -2573029715775428776L;

    private String memberName;

    private Integer gender;

    /**
     * phone format: 8613131693996
     */
    private String phone;

    private String email;

    private Long cityId;

    private Long areaId;

    private String detail;

    private String reference;

    private String extra;

    public AddressInsertParam() {
    }

    public AddressInsertParam(String memberName, Integer gender, String phone, String email, Long cityId, Long areaId, String detail, String reference, String extra) {
        this.memberName = memberName;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.cityId = cityId;
        this.areaId = areaId;
        this.detail = detail;
        this.reference = reference;
        this.extra = extra;
    }

    @Override
    public void asserts() {
        if (isBlank(this.memberName))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "memberName can't be blank");
        assertGenderIdentity(this.gender, false);
        if (isBlank(this.phone))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "phone can't be blank");
        if (isNull(this.cityId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "cityId can't be null");
        if (isBlank(this.detail))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "detail can't be blank");
        if (isBlank(this.reference))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "reference can't be blank");
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
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

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    @Override
    public String toString() {
        return "AddressInsertParam{" +
                "memberName='" + memberName + '\'' +
                ", gender=" + gender +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", cityId=" + cityId +
                ", areaId=" + areaId +
                ", detail='" + detail + '\'' +
                ", reference='" + reference + '\'' +
                ", extra='" + extra + '\'' +
                '}';
    }

}

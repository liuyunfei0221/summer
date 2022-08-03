package com.blue.member.model;

import com.blue.basic.inter.Asserter;
import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isBlank;
import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.common.base.ConstantProcessor.assertGender;
import static com.blue.basic.constant.common.ResponseElement.INVALID_PARAM;
import static com.blue.basic.constant.member.BlueMemberThreshold.*;

/**
 * params for insert a new address
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public class AddressInsertParam implements Serializable, Asserter {

    private static final long serialVersionUID = -2573029715775428776L;

    protected String contact;

    protected Integer gender;

    /**
     * phone format: 8613131693996
     */
    protected String phone;

    protected String email;

    protected Long cityId;

    protected Long areaId;

    protected String detail;

    protected String reference;

    protected String extra;

    public AddressInsertParam() {
    }

    public AddressInsertParam(String contact, Integer gender, String phone, String email, Long cityId, Long areaId, String detail, String reference, String extra) {
        this.contact = contact;
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
        if (isBlank(this.contact) || isBlank(this.phone) || isNull(this.cityId) || isBlank(this.detail) || isBlank(this.reference))
            throw new BlueException(INVALID_PARAM);
        assertGender(this.gender, false);

        int length = contact.length();
        if (length < CONTACT_LEN_MIN.value || length > CONTACT_LEN_MAX.value)
            throw new BlueException(INVALID_PARAM);

        length = phone.length();
        if (length < PHONE_LEN_MIN.value || length > PHONE_LEN_MAX.value)
            throw new BlueException(INVALID_PARAM);

        if (isNull(this.cityId))
            throw new BlueException(INVALID_PARAM);

        length = detail.length();
        if (length < ADDR_DETAIL_LEN_MIN.value || length > ADDR_DETAIL_LEN_MAX.value)
            throw new BlueException(INVALID_PARAM);

        length = reference.length();
        if (length < REFERENCE_LEN_MIN.value || length > REFERENCE_LEN_MAX.value)
            throw new BlueException(INVALID_PARAM);
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
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
                "contact='" + contact + '\'' +
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

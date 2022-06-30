package com.blue.member.api.model;

import com.blue.base.constant.common.BlueCommonThreshold;
import com.blue.base.inter.Asserter;
import com.blue.base.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.base.common.base.BlueChecker.isBlank;
import static com.blue.base.common.base.BlueChecker.isNotBlank;
import static com.blue.base.constant.common.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.common.ResponseElement.VERIFY_IS_INVALID;

/**
 * member registry params
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "WeakerAccess", "AliControlFlowStatementWithoutBraces"})
public final class MemberRegistryParam implements Serializable, Asserter {

    private static final long serialVersionUID = 8543617230220651524L;

    private String phone;

    private String phoneVerify;

    private String email;

    private String emailVerify;

    private String access;

    private String name;

    private String icon;

    /**
     * @see com.blue.base.constant.member.Gender
     */
    private Integer gender;

    /**
     * @see com.blue.base.constant.member.SourceType
     */
    private String source;

    public MemberRegistryParam() {
    }

    @Override
    public void asserts() {
        if (isBlank(this.phone) || isBlank(this.email) || isBlank(this.access) || isBlank(this.name))
            throw new BlueException(BAD_REQUEST);

        if (isBlank(this.phoneVerify) || isBlank(this.emailVerify))
            throw new BlueException(VERIFY_IS_INVALID);

        if (isNotBlank(this.access)) {
            if (this.access.length() > BlueCommonThreshold.ACS_LEN_MAX.value)
                throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "access length is too long");
            if (this.access.length() < BlueCommonThreshold.ACS_LEN_MIN.value)
                throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "access length is too short");
        }
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneVerify() {
        return phoneVerify;
    }

    public void setPhoneVerify(String phoneVerify) {
        this.phoneVerify = phoneVerify;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailVerify() {
        return emailVerify;
    }

    public void setEmailVerify(String emailVerify) {
        this.emailVerify = emailVerify;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "MemberRegistryParam{" +
                "phone='" + phone + '\'' +
                ", phoneVerify='" + phoneVerify + '\'' +
                ", email='" + email + '\'' +
                ", emailVerify='" + emailVerify + '\'' +
                ", access='" + access + '\'' +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", gender=" + gender +
                ", source=" + source +
                '}';
    }

}

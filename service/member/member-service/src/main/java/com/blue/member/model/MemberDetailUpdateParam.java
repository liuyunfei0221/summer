package com.blue.member.model;

import com.blue.base.inter.Asserter;
import com.blue.base.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.common.base.ConstantProcessor.assertGenderIdentity;
import static com.blue.base.constant.common.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.member.MemberThreshold.*;

/**
 * params for update an exist member detail
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public class MemberDetailUpdateParam implements Serializable, Asserter {

    private static final long serialVersionUID = 4028727042786997123L;

    private Long id;

    private String name;

    private Integer gender;

    /**
     * phone format: 8613131693996
     */
    private String phone;

    private String email;

    private Integer yearOfBirth;

    private Integer monthOfBirth;

    private Integer dayOfBirth;

    private Integer height;

    private Integer weight;

    private Long cityId;

    private String address;

    private String profile;

    private String hobby;

    private String homepage;

    private String extra;

    public MemberDetailUpdateParam() {
    }

    public MemberDetailUpdateParam(Long id, String name, Integer gender, String phone, String email,
                                   Integer yearOfBirth, Integer monthOfBirth, Integer dayOfBirth,
                                   Integer height, Integer weight, Long cityId, String address,
                                   String profile, String hobby, String homepage, String extra) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.yearOfBirth = yearOfBirth;
        this.monthOfBirth = monthOfBirth;
        this.dayOfBirth = dayOfBirth;
        this.height = height;
        this.weight = weight;
        this.cityId = cityId;
        this.address = address;
        this.profile = profile;
        this.hobby = hobby;
        this.homepage = homepage;
        this.extra = extra;
    }

    @Override
    public void asserts() {
        if (isInvalidIdentity(this.id))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid id");
        if (isBlank(this.name))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "name can't be blank");
        assertGenderIdentity(this.gender, false);
        if (isBlank(this.phone))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "phone can't be blank");
        if (isBlank(this.email))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "email can't be blank");
        if (isNull(this.yearOfBirth) || this.yearOfBirth < MIN_YEAR_OF_BIRTH.threshold)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid year of birth");
        if (isNull(this.monthOfBirth) || this.monthOfBirth < MIN_MONTH_OF_BIRTH.threshold || this.monthOfBirth > MAX_MONTH_OF_BIRTH.threshold)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid month of birth");
        if (isNull(this.dayOfBirth) || this.dayOfBirth < MIN_DAY_OF_BIRTH.threshold || this.dayOfBirth > MAX_DAY_OF_BIRTH.threshold)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid day of birth");
        if (isNull(this.height) || this.height < 1L)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid height");
        if (isNull(this.weight) || this.weight < 1L)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid weight");
        if (isInvalidIdentity(this.cityId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid city id");
        if (isBlank(this.address))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "phone can't be blank");
        if (isBlank(this.profile))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "email can't be blank");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(Integer yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public Integer getMonthOfBirth() {
        return monthOfBirth;
    }

    public void setMonthOfBirth(Integer monthOfBirth) {
        this.monthOfBirth = monthOfBirth;
    }

    public Integer getDayOfBirth() {
        return dayOfBirth;
    }

    public void setDayOfBirth(Integer dayOfBirth) {
        this.dayOfBirth = dayOfBirth;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    @Override
    public String toString() {
        return "MemberDetailUpdateParam{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", yearOfBirth=" + yearOfBirth +
                ", monthOfBirth=" + monthOfBirth +
                ", dayOfBirth=" + dayOfBirth +
                ", height=" + height +
                ", weight=" + weight +
                ", cityId=" + cityId +
                ", address='" + address + '\'' +
                ", profile='" + profile + '\'' +
                ", hobby='" + hobby + '\'' +
                ", homepage='" + homepage + '\'' +
                ", extra='" + extra + '\'' +
                '}';
    }

}

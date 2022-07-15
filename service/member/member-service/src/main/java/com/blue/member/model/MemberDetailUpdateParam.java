package com.blue.member.model;

import com.blue.basic.inter.Asserter;
import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isBlank;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;

/**
 * params for update an exist member detail
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public class MemberDetailUpdateParam implements Serializable, Asserter {

    private static final long serialVersionUID = 4028727042786997123L;

    private String name;

    private Integer gender;

    /**
     * phone format: 8613131693996
     */
    private String phone;

    private String email;

    private String birthDay;

    private Integer chineseZodiac;

    private Integer zodiacSign;

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

    public MemberDetailUpdateParam(String name, Integer gender, String phone, String email, String birthDay, Integer chineseZodiac, Integer zodiacSign,
                                   Integer height, Integer weight, Long cityId, String address, String profile, String hobby, String homepage, String extra) {
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.birthDay = birthDay;
        this.chineseZodiac = chineseZodiac;
        this.zodiacSign = zodiacSign;
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
        if (isBlank(this.name))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid name");
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

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public Integer getChineseZodiac() {
        return chineseZodiac;
    }

    public void setChineseZodiac(Integer chineseZodiac) {
        this.chineseZodiac = chineseZodiac;
    }

    public Integer getZodiacSign() {
        return zodiacSign;
    }

    public void setZodiacSign(Integer zodiacSign) {
        this.zodiacSign = zodiacSign;
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
                "name='" + name + '\'' +
                ", gender=" + gender +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", birthDay='" + birthDay + '\'' +
                ", chineseZodiac=" + chineseZodiac +
                ", zodiacSign=" + zodiacSign +
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

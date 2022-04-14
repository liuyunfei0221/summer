package com.blue.member.api.model;

import java.io.Serializable;


/**
 * member detail info
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public final class MemberDetailInfo implements Serializable {

    private static final long serialVersionUID = -3977258686475948793L;
    
    private Long id;

    private Long memberId;

    private String name;

    private Integer gender;

    /**
     * phone format: 86-13131693996
     */
    private String phone;

    private String email;

    private Long countryId;

    private String country;

    private Long stateId;

    private String state;

    private Long cityId;

    private String city;

    private String address;

    private String profile;

    private String hobby;

    private String homepage;

    private String extra;

    public MemberDetailInfo() {
    }

    public MemberDetailInfo(Long id, Long memberId, String name, Integer gender, String phone, String email, Long countryId, String country,
                            Long stateId, String state, Long cityId, String city, String address, String profile, String hobby, String homepage, String extra) {
        this.id = id;
        this.memberId = memberId;
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.countryId = countryId;
        this.country = country;
        this.stateId = stateId;
        this.state = state;
        this.cityId = cityId;
        this.city = city;
        this.address = address;
        this.profile = profile;
        this.hobby = hobby;
        this.homepage = homepage;
        this.extra = extra;
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

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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
        return "MemberDetailInfo{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", countryId=" + countryId +
                ", country='" + country + '\'' +
                ", stateId=" + stateId +
                ", state='" + state + '\'' +
                ", cityId=" + cityId +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", profile='" + profile + '\'' +
                ", hobby='" + hobby + '\'' +
                ", homepage='" + homepage + '\'' +
                ", extra='" + extra + '\'' +
                '}';
    }

}

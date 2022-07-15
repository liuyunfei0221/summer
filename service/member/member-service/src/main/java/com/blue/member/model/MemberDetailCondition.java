package com.blue.member.model;

import com.blue.basic.constant.common.SortType;
import com.blue.basic.model.common.SortCondition;
import com.blue.member.constant.MemberDetailSortAttribute;

import java.io.Serializable;

/**
 * member detail condition for select
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class MemberDetailCondition extends SortCondition implements Serializable {

    private static final long serialVersionUID = -5961680212338876121L;

    private Long id;

    private Long memberId;

    private String name;

    private Integer gender;

    private String phone;

    private String email;

    private Integer yearOfBirth;

    private Integer monthOfBirth;

    private Integer dayOfBirth;

    private Integer chineseZodiac;

    private Integer zodiacSign;

    private Integer height;

    private Integer weight;

    private Long countryId;

    private Long stateId;

    private Long cityId;

    private String address;

    private String profile;

    private String hobbyLike;

    private String homepage;

    /**
     * @see com.blue.basic.constant.common.Status
     */
    private Integer status;

    private Long createTimeBegin;

    private Long createTimeEnd;

    private Long updateTimeBegin;

    private Long updateTimeEnd;

    public MemberDetailCondition() {
        super(MemberDetailSortAttribute.ID.attribute, SortType.DESC.identity);
    }

    public MemberDetailCondition(String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
    }

    public MemberDetailCondition(Long id, Long memberId, String name, Integer gender, String phone, String email, Integer yearOfBirth, Integer monthOfBirth, Integer dayOfBirth,
                                 Integer chineseZodiac, Integer zodiacSign, Integer height, Integer weight, Long countryId, Long stateId, Long cityId, String address,
                                 String profile, String hobbyLike, String homepage, Integer status, Long createTimeBegin, Long createTimeEnd, Long updateTimeBegin, Long updateTimeEnd,
                                 String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
        this.id = id;
        this.memberId = memberId;
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.yearOfBirth = yearOfBirth;
        this.monthOfBirth = monthOfBirth;
        this.dayOfBirth = dayOfBirth;
        this.chineseZodiac = chineseZodiac;
        this.zodiacSign = zodiacSign;
        this.height = height;
        this.weight = weight;
        this.countryId = countryId;
        this.stateId = stateId;
        this.cityId = cityId;
        this.address = address;
        this.profile = profile;
        this.hobbyLike = hobbyLike;
        this.homepage = homepage;
        this.status = status;
        this.createTimeBegin = createTimeBegin;
        this.createTimeEnd = createTimeEnd;
        this.updateTimeBegin = updateTimeBegin;
        this.updateTimeEnd = updateTimeEnd;
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

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
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

    public String getHobbyLike() {
        return hobbyLike;
    }

    public void setHobbyLike(String hobbyLike) {
        this.hobbyLike = hobbyLike;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCreateTimeBegin() {
        return createTimeBegin;
    }

    public void setCreateTimeBegin(Long createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }

    public Long getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(Long createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public Long getUpdateTimeBegin() {
        return updateTimeBegin;
    }

    public void setUpdateTimeBegin(Long updateTimeBegin) {
        this.updateTimeBegin = updateTimeBegin;
    }

    public Long getUpdateTimeEnd() {
        return updateTimeEnd;
    }

    public void setUpdateTimeEnd(Long updateTimeEnd) {
        this.updateTimeEnd = updateTimeEnd;
    }

    @Override
    public String toString() {
        return "MemberDetailCondition{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", yearOfBirth=" + yearOfBirth +
                ", monthOfBirth=" + monthOfBirth +
                ", dayOfBirth=" + dayOfBirth +
                ", chineseZodiac=" + chineseZodiac +
                ", zodiacSign=" + zodiacSign +
                ", height=" + height +
                ", weight=" + weight +
                ", countryId=" + countryId +
                ", stateId=" + stateId +
                ", cityId=" + cityId +
                ", address='" + address + '\'' +
                ", profile='" + profile + '\'' +
                ", hobbyLike='" + hobbyLike + '\'' +
                ", homepage='" + homepage + '\'' +
                ", status=" + status +
                ", createTimeBegin=" + createTimeBegin +
                ", createTimeEnd=" + createTimeEnd +
                ", updateTimeBegin=" + updateTimeBegin +
                ", updateTimeEnd=" + updateTimeEnd +
                '}';
    }

}

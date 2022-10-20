package com.blue.shine.api.model;

import java.io.Serializable;

/**
 * shine info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class ShineInfo implements Serializable {

    private static final long serialVersionUID = 4417623130276820000L;

    private Long id;

    private String title;

    private String content;

    private String detail;

    private String contact;

    private String contactDetail;

    private Long countryId;

    private String country;

    private Long stateId;

    private String state;

    private Long cityId;

    private String city;

    private String addressDetail;

    private String extra;

    private Integer priority;

    private Long createTime;

    public ShineInfo() {
    }

    public ShineInfo(Long id, String title, String content, String detail, String contact, String contactDetail, Long countryId, String country, Long stateId,
                     String state, Long cityId, String city, String addressDetail, String extra, Integer priority, Long createTime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.detail = detail;
        this.contact = contact;
        this.contactDetail = contactDetail;
        this.countryId = countryId;
        this.country = country;
        this.stateId = stateId;
        this.state = state;
        this.cityId = cityId;
        this.city = city;
        this.addressDetail = addressDetail;
        this.extra = extra;
        this.priority = priority;
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactDetail() {
        return contactDetail;
    }

    public void setContactDetail(String contactDetail) {
        this.contactDetail = contactDetail;
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

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ShineInfo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", detail='" + detail + '\'' +
                ", contact='" + contact + '\'' +
                ", contactDetail='" + contactDetail + '\'' +
                ", countryId=" + countryId +
                ", country='" + country + '\'' +
                ", stateId=" + stateId +
                ", state='" + state + '\'' +
                ", cityId=" + cityId +
                ", city='" + city + '\'' +
                ", addressDetail='" + addressDetail + '\'' +
                ", extra='" + extra + '\'' +
                ", priority=" + priority +
                ", createTime=" + createTime +
                '}';
    }

}

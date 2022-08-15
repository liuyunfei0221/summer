package com.blue.shine.repository.entity;

import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * shine info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public class Shine implements Serializable {

    private static final long serialVersionUID = -5386491585245489265L;

    @Id
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

    private Long updateTime;

    private Long creator;

    private Long updater;

    public Shine() {
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

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public Long getUpdater() {
        return updater;
    }

    public void setUpdater(Long updater) {
        this.updater = updater;
    }

    @Override
    public String toString() {
        return "Shine{" +
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
                ", updateTime=" + updateTime +
                ", creator=" + creator +
                ", updater=" + updater +
                '}';
    }

}

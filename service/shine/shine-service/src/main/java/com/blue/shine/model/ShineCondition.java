package com.blue.shine.model;

import com.blue.basic.constant.common.SortType;
import com.blue.basic.model.common.SortCondition;
import com.blue.shine.constant.ShineSortAttribute;

import java.io.Serializable;

/**
 * shine condition for select
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class ShineCondition extends SortCondition implements Serializable {

    private static final long serialVersionUID = -7416282265362829773L;

    private Long id;

    private String titleLike;

    private String contentLike;

    private String detailLike;

    private String contactLike;

    private String contactDetailLike;

    private Long countryId;

    private Long stateId;

    private Long cityId;

    private String addressDetailLike;

    private String extra;

    private Integer order;

    private Long createTimeBegin;

    private Long createTimeEnd;

    private Long updateTimeBegin;

    private Long updateTimeEnd;

    private Long creator;

    private Long updater;

    public ShineCondition() {
        super(ShineSortAttribute.ID.attribute, SortType.DESC.identity);
    }

    public ShineCondition(String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
    }

    public ShineCondition(Long id, String titleLike, String contentLike, String detailLike, String contactLike, String contactDetailLike, Long countryId, Long stateId, Long cityId, String addressDetailLike,
                          String extra, Integer order, Long createTimeBegin, Long createTimeEnd, Long updateTimeBegin, Long updateTimeEnd, Long creator, Long updater, String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
        this.id = id;
        this.titleLike = titleLike;
        this.contentLike = contentLike;
        this.detailLike = detailLike;
        this.contactLike = contactLike;
        this.contactDetailLike = contactDetailLike;
        this.countryId = countryId;
        this.stateId = stateId;
        this.cityId = cityId;
        this.addressDetailLike = addressDetailLike;
        this.extra = extra;
        this.order = order;
        this.createTimeBegin = createTimeBegin;
        this.createTimeEnd = createTimeEnd;
        this.updateTimeBegin = updateTimeBegin;
        this.updateTimeEnd = updateTimeEnd;
        this.creator = creator;
        this.updater = updater;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitleLike() {
        return titleLike;
    }

    public void setTitleLike(String titleLike) {
        this.titleLike = titleLike;
    }

    public String getContentLike() {
        return contentLike;
    }

    public void setContentLike(String contentLike) {
        this.contentLike = contentLike;
    }

    public String getDetailLike() {
        return detailLike;
    }

    public void setDetailLike(String detailLike) {
        this.detailLike = detailLike;
    }

    public String getContactLike() {
        return contactLike;
    }

    public void setContactLike(String contactLike) {
        this.contactLike = contactLike;
    }

    public String getContactDetailLike() {
        return contactDetailLike;
    }

    public void setContactDetailLike(String contactDetailLike) {
        this.contactDetailLike = contactDetailLike;
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

    public String getAddressDetailLike() {
        return addressDetailLike;
    }

    public void setAddressDetailLike(String addressDetailLike) {
        this.addressDetailLike = addressDetailLike;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
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
        return "ShineCondition{" +
                "id=" + id +
                ", titleLike='" + titleLike + '\'' +
                ", contentLike='" + contentLike + '\'' +
                ", detailLike='" + detailLike + '\'' +
                ", contactLike='" + contactLike + '\'' +
                ", contactDetailLike='" + contactDetailLike + '\'' +
                ", countryId=" + countryId +
                ", stateId=" + stateId +
                ", cityId=" + cityId +
                ", addressDetailLike='" + addressDetailLike + '\'' +
                ", extra='" + extra + '\'' +
                ", order=" + order +
                ", createTimeBegin=" + createTimeBegin +
                ", createTimeEnd=" + createTimeEnd +
                ", updateTimeBegin=" + updateTimeBegin +
                ", updateTimeEnd=" + updateTimeEnd +
                ", creator=" + creator +
                ", updater=" + updater +
                ", sortAttribute='" + sortAttribute + '\'' +
                ", sortType='" + sortType + '\'' +
                '}';
    }

}

package com.blue.member.model;

import com.blue.basic.constant.common.SortType;
import com.blue.basic.model.common.SortCondition;
import com.blue.member.constant.AddressSortAttribute;

import java.io.Serializable;

/**
 * address condition for select
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class AddressCondition extends SortCondition implements Serializable {

    private static final long serialVersionUID = -7022130148038680234L;

    private Long id;

    private Long memberId;

    private String memberNameLike;

    /**
     * @see com.blue.basic.constant.member.Gender
     */
    private Integer gender;

    /**
     * phone format: 8613131693996
     */
    private String phoneLike;

    private String emailLike;

    private Long countryId;

    private Long stateId;

    private Long cityId;

    private Long areaId;

    private String detailLike;

    private String referenceLike;

    /**
     * @see com.blue.basic.constant.common.Status
     */
    private Integer status;

    private Long createTimeBegin;

    private Long createTimeEnd;

    private Long updateTimeBegin;

    private Long updateTimeEnd;

    public AddressCondition() {
        super(AddressSortAttribute.CREATE_TIME.attribute, SortType.DESC.identity);
    }

    public AddressCondition(String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
    }

    public AddressCondition(Long id, Long memberId, String memberNameLike, Integer gender, String phoneLike, String emailLike, Long countryId,
                            Long stateId, Long cityId, Long areaId, String detailLike, String referenceLike, Integer status, Long createTimeBegin,
                            Long createTimeEnd, Long updateTimeBegin, Long updateTimeEnd, String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
        this.id = id;
        this.memberId = memberId;
        this.memberNameLike = memberNameLike;
        this.gender = gender;
        this.phoneLike = phoneLike;
        this.emailLike = emailLike;
        this.countryId = countryId;
        this.stateId = stateId;
        this.cityId = cityId;
        this.areaId = areaId;
        this.detailLike = detailLike;
        this.referenceLike = referenceLike;
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

    public String getMemberNameLike() {
        return memberNameLike;
    }

    public void setMemberNameLike(String memberNameLike) {
        this.memberNameLike = memberNameLike;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getPhoneLike() {
        return phoneLike;
    }

    public void setPhoneLike(String phoneLike) {
        this.phoneLike = phoneLike;
    }

    public String getEmailLike() {
        return emailLike;
    }

    public void setEmailLike(String emailLike) {
        this.emailLike = emailLike;
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

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getDetailLike() {
        return detailLike;
    }

    public void setDetailLike(String detailLike) {
        this.detailLike = detailLike;
    }

    public String getReferenceLike() {
        return referenceLike;
    }

    public void setReferenceLike(String referenceLike) {
        this.referenceLike = referenceLike;
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
        return "AddressCondition{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", memberNameLike='" + memberNameLike + '\'' +
                ", gender=" + gender +
                ", phoneLike='" + phoneLike + '\'' +
                ", emailLike='" + emailLike + '\'' +
                ", countryId=" + countryId +
                ", stateId=" + stateId +
                ", cityId=" + cityId +
                ", areaId=" + areaId +
                ", detailLike='" + detailLike + '\'' +
                ", referenceLike='" + referenceLike + '\'' +
                ", status=" + status +
                ", createTimeBegin=" + createTimeBegin +
                ", createTimeEnd=" + createTimeEnd +
                ", updateTimeBegin=" + updateTimeBegin +
                ", updateTimeEnd=" + updateTimeEnd +
                ", sortAttribute='" + sortAttribute + '\'' +
                ", sortType='" + sortType + '\'' +
                '}';
    }
}

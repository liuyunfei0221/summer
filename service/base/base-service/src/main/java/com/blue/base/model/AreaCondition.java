package com.blue.base.model;

import java.io.Serializable;

/**
 * area condition for select
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class AreaCondition implements Serializable {

    private static final long serialVersionUID = 2966321684062786198L;

    private Long id;

    private Long countryId;

    private Long stateId;

    private Long cityId;

    private String nameLike;

    public AreaCondition() {
    }

    public AreaCondition(Long id, Long countryId, Long stateId, Long cityId, String nameLike) {
        this.id = id;
        this.countryId = countryId;
        this.stateId = stateId;
        this.cityId = cityId;
        this.nameLike = nameLike;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getNameLike() {
        return nameLike;
    }

    public void setNameLike(String nameLike) {
        this.nameLike = nameLike;
    }

    @Override
    public String toString() {
        return "AreaCondition{" +
                "id=" + id +
                ", countryId=" + countryId +
                ", stateId=" + stateId +
                ", cityId=" + cityId +
                ", nameLike='" + nameLike + '\'' +
                '}';
    }

}

package com.blue.base.model;

import java.io.Serializable;

/**
 * city condition for select
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class CityCondition implements Serializable {

    private static final long serialVersionUID = 6565138545340327590L;

    private Long id;

    private Long countryId;

    private Long stateId;

    private String nameLike;

    private Integer status;

    public CityCondition() {
    }

    public CityCondition(Long id, Long countryId, Long stateId, String nameLike, Integer status) {
        this.id = id;
        this.countryId = countryId;
        this.stateId = stateId;
        this.nameLike = nameLike;
        this.status = status;
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

    public String getNameLike() {
        return nameLike;
    }

    public void setNameLike(String nameLike) {
        this.nameLike = nameLike;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "CityCondition{" +
                "id=" + id +
                ", countryId=" + countryId +
                ", stateId=" + stateId +
                ", nameLike='" + nameLike + '\'' +
                ", status=" + status +
                '}';
    }

}

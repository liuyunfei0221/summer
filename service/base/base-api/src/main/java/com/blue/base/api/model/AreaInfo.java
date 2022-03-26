package com.blue.base.api.model;

import java.io.Serializable;

/**
 * area info
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class AreaInfo implements Serializable {

    private static final long serialVersionUID = 6113836087899954124L;

    private Long id;

    private Long countryId;

    private Long stateId;

    private Long cityId;

    private String name;

    public AreaInfo() {
    }

    public AreaInfo(Long id, Long countryId, Long stateId, Long cityId, String name) {
        this.id = id;
        this.countryId = countryId;
        this.stateId = stateId;
        this.cityId = cityId;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AreaInfo{" +
                "id=" + id +
                ", countryId=" + countryId +
                ", stateId=" + stateId +
                ", cityId=" + cityId +
                ", name='" + name + '\'' +
                '}';
    }

}

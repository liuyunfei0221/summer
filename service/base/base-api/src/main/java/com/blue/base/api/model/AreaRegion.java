package com.blue.base.api.model;

import java.io.Serializable;

/**
 * area region
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class AreaRegion implements Serializable {

    private static final long serialVersionUID = -1143294403062049860L;

    private Long areaId;

    private CountryInfo country;

    private StateInfo state;

    private CityInfo city;

    private AreaInfo area;

    public AreaRegion() {
    }

    public AreaRegion(Long areaId, CountryInfo country, StateInfo state, CityInfo city, AreaInfo area) {
        this.areaId = areaId;
        this.country = country;
        this.state = state;
        this.city = city;
        this.area = area;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public CountryInfo getCountry() {
        return country;
    }

    public void setCountry(CountryInfo country) {
        this.country = country;
    }

    public StateInfo getState() {
        return state;
    }

    public void setState(StateInfo state) {
        this.state = state;
    }

    public CityInfo getCity() {
        return city;
    }

    public void setCity(CityInfo city) {
        this.city = city;
    }

    public AreaInfo getArea() {
        return area;
    }

    public void setArea(AreaInfo area) {
        this.area = area;
    }

    @Override
    public String toString() {
        return "AreaRegion{" +
                "areaId=" + areaId +
                ", country=" + country +
                ", state=" + state +
                ", city=" + city +
                ", area=" + area +
                '}';
    }

}

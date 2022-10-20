package com.blue.base.api.model;

import java.io.Serializable;

/**
 * city region
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class CityRegion implements Serializable {

    private static final long serialVersionUID = -8107608086540197619L;

    private Long cityId;

    private CountryInfo country;

    private StateInfo state;

    private CityInfo city;

    public CityRegion() {
    }

    public CityRegion(Long cityId, CountryInfo country, StateInfo state, CityInfo city) {
        this.cityId = cityId;
        this.country = country;
        this.state = state;
        this.city = city;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
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

    @Override
    public String toString() {
        return "CityRegion{" +
                "cityId=" + cityId +
                ", country=" + country +
                ", state=" + state +
                ", city=" + city +
                '}';
    }

}

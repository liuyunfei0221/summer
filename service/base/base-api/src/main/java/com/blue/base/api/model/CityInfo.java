package com.blue.base.api.model;

import com.blue.basic.serializer.Double2StringSerializer;
import com.blue.basic.serializer.Long2StringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * city info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class CityInfo implements Serializable {

    private static final long serialVersionUID = -1328565587446491452L;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long id;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long countryId;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long stateId;

    private String name;

    @JsonSerialize(using = Double2StringSerializer.class)
    private Double longitude;

    @JsonSerialize(using = Double2StringSerializer.class)
    private Double latitude;

    public CityInfo() {
    }

    public CityInfo(Long id, Long countryId, Long stateId, String name, Double longitude, Double latitude) {
        this.id = id;
        this.countryId = countryId;
        this.stateId = stateId;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "CityInfo{" +
                "id=" + id +
                ", countryId=" + countryId +
                ", stateId=" + stateId +
                ", name='" + name + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }

}

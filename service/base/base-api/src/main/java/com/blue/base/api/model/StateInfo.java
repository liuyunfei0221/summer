package com.blue.base.api.model;

import com.blue.basic.serializer.Double2StringSerializer;
import com.blue.basic.serializer.Long2StringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * state info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class StateInfo implements Serializable {

    private static final long serialVersionUID = 8273318922366078046L;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long id;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long countryId;

    private String name;

    private String fipsCode;

    private String stateCode;

    @JsonSerialize(using = Double2StringSerializer.class)
    private Double longitude;

    @JsonSerialize(using = Double2StringSerializer.class)
    private Double latitude;

    public StateInfo() {
    }

    public StateInfo(Long id, Long countryId, String name, String fipsCode, String stateCode, Double longitude, Double latitude) {
        this.id = id;
        this.countryId = countryId;
        this.name = name;
        this.fipsCode = fipsCode;
        this.stateCode = stateCode;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFipsCode() {
        return fipsCode;
    }

    public void setFipsCode(String fipsCode) {
        this.fipsCode = fipsCode;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
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
        return "StateInfo{" +
                "id=" + id +
                ", countryId=" + countryId +
                ", name='" + name + '\'' +
                ", fipsCode='" + fipsCode + '\'' +
                ", stateCode='" + stateCode + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }

}

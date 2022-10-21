package com.blue.base.api.model;

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

    public StateInfo() {
    }

    public StateInfo(Long id, Long countryId, String name, String fipsCode, String stateCode) {
        this.id = id;
        this.countryId = countryId;
        this.name = name;
        this.fipsCode = fipsCode;
        this.stateCode = stateCode;
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

    @Override
    public String toString() {
        return "StateInfo{" +
                "id=" + id +
                ", countryId=" + countryId +
                ", name='" + name + '\'' +
                ", fipsCode='" + fipsCode + '\'' +
                ", stateCode='" + stateCode + '\'' +
                '}';
    }

}

package com.blue.base.api.model;

import java.io.Serializable;

/**
 * state info
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class StateInfo implements Serializable {

    private static final long serialVersionUID = 8273318922366078046L;

    private Long id;

    private Long countryId;

    private String name;

    private String fipsCode;

    private String countryCode;

    private String stateCode;

    public StateInfo() {
    }

    public StateInfo(Long id, Long countryId, String name, String fipsCode, String countryCode, String stateCode) {
        this.id = id;
        this.countryId = countryId;
        this.name = name;
        this.fipsCode = fipsCode;
        this.countryCode = countryCode;
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

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
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
                ", countryCode='" + countryCode + '\'' +
                ", stateCode='" + stateCode + '\'' +
                '}';
    }

}

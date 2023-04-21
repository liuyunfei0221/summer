package com.blue.base.model;

import com.blue.basic.inter.Asserter;
import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;

/**
 * params for insert a new state
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public class StateInsertParam implements Serializable, Asserter {

    private static final long serialVersionUID = 969597394384394829L;

    protected Long countryId;

    protected String name;

    protected String fipsCode;

    protected String stateCode;

    protected Double longitude;

    protected Double latitude;

    public StateInsertParam() {
    }

    public StateInsertParam(Long countryId, String name, String fipsCode, String stateCode, Double longitude, Double latitude) {
        this.countryId = countryId;
        this.name = name;
        this.fipsCode = fipsCode;
        this.stateCode = stateCode;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    public void asserts() {
        if (isInvalidIdentity(this.countryId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "countryId can't be null or less than 1");
        if (isBlank(this.name))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "name can't be blank");
        if (isBlank(this.fipsCode))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "fipsCode can't be blank");
        if (isBlank(this.stateCode))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "stateCode can't be blank");
        if (isNull(this.longitude))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "longitude can't be null");
        if (isNull(this.latitude))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "latitude can't be null");
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
        return "StateInsertParam{" +
                "countryId=" + countryId +
                ", name='" + name + '\'' +
                ", fipsCode='" + fipsCode + '\'' +
                ", stateCode='" + stateCode + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }

}

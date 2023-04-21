package com.blue.base.model;

import com.blue.basic.inter.Asserter;
import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;

/**
 * params for insert a new area
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces", "DuplicatedCode"})
public class AreaInsertParam implements Serializable, Asserter {

    private static final long serialVersionUID = -3747454352086271819L;

    protected Long cityId;

    protected String name;

    protected Double longitude;

    protected Double latitude;

    public AreaInsertParam() {
    }

    public AreaInsertParam(Long cityId, String name, Double longitude, Double latitude) {
        this.cityId = cityId;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    public void asserts() {
        if (isInvalidIdentity(this.cityId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "cityId can't be null or less than 1");
        if (isBlank(this.name))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "name can't be blank");
        if (isNull(this.longitude))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "longitude can't be null");
        if (isNull(this.latitude))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "latitude can't be null");
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
        return "AreaInsertParam{" +
                "cityId=" + cityId +
                ", name='" + name + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }

}

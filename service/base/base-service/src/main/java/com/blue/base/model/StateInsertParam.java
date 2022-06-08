package com.blue.base.model;

import com.blue.base.inter.Asserter;
import com.blue.base.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.base.common.base.BlueChecker.isBlank;
import static com.blue.base.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.base.constant.common.ResponseElement.BAD_REQUEST;

/**
 * params for insert a new state
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public class StateInsertParam implements Serializable, Asserter {

    private static final long serialVersionUID = 969597394384394829L;

    private Long countryId;

    private String name;

    private String fipsCode;

    private String stateCode;

    public StateInsertParam() {
    }

    public StateInsertParam(Long countryId, String name, String fipsCode, String stateCode) {
        this.countryId = countryId;
        this.name = name;
        this.fipsCode = fipsCode;
        this.stateCode = stateCode;
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
        return "StateInsertParam{" +
                "countryId=" + countryId +
                ", name='" + name + '\'' +
                ", fipsCode='" + fipsCode + '\'' +
                ", stateCode='" + stateCode + '\'' +
                '}';
    }

}

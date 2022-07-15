package com.blue.base.model;

import com.blue.basic.inter.Asserter;
import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isBlank;
import static com.blue.basic.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;

/**
 * params for insert a new city
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public class CityInsertParam implements Serializable, Asserter {

    private static final long serialVersionUID = 1833652005271327501L;

    private Long stateId;

    private String name;

    public CityInsertParam() {
    }

    public CityInsertParam(Long stateId, String name) {
        this.stateId = stateId;
        this.name = name;
    }

    @Override
    public void asserts() {
        if (isInvalidIdentity(this.stateId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "stateId can't be null or less than 1");
        if (isBlank(this.name))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "name can't be blank");
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

    @Override
    public String toString() {
        return "CityInsertParam{" +
                "stateId=" + stateId +
                ", name='" + name + '\'' +
                '}';
    }

}

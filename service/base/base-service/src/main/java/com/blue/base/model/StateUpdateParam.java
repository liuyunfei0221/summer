package com.blue.base.model;

import com.blue.basic.model.exps.BlueException;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;

/**
 * params for update state
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class StateUpdateParam extends StateInsertParam {

    private static final long serialVersionUID = 8599834639655114714L;

    private Long id;

    public StateUpdateParam() {
    }

    public StateUpdateParam(Long id, Long countryId, String name, String fipsCode, String stateCode) {
        super(countryId, name, fipsCode, stateCode);
        this.id = id;
    }

    @Override
    public void asserts() {
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        if (isNull(super.getCountryId()) && isBlank(super.getName()) && isBlank(super.getFipsCode()) && isBlank(super.getStateCode()))
            throw new BlueException(EMPTY_PARAM);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "CityUpdateParam{" +
                "id=" + id +
                ", countryId=" + super.getCountryId() +
                ", name='" + super.getName() + '\'' +
                ", fipsCode='" + super.getFipsCode() + '\'' +
                ", stateCode='" + super.getStateCode() + '\'' +
                '}';
    }
}

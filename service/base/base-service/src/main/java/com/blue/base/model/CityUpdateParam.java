package com.blue.base.model;

import com.blue.basic.model.exps.BlueException;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;

/**
 * params for update city
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class CityUpdateParam extends CityInsertParam {

    private static final long serialVersionUID = -6170586743588178923L;

    private Long id;

    public CityUpdateParam() {
    }

    public CityUpdateParam(Long id, Long stateId, String name, Double longitude, Double latitude) {
        super(stateId, name, longitude, latitude);
        this.id = id;
    }

    @Override
    public void asserts() {
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        if (isNull(super.getStateId()) && isBlank(super.getName())&&
                isNull(super.getLongitude()) && isNull(super.getLatitude()))
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
                ", stateId=" + stateId +
                ", name='" + name + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }

}

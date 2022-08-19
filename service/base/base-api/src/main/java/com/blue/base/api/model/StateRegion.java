package com.blue.base.api.model;

import com.blue.basic.serializer.IdentityDeserializer;
import com.blue.basic.serializer.IdentitySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * state region
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class StateRegion implements Serializable {

    private static final long serialVersionUID = 8341453968629415940L;

    @JsonSerialize(using = IdentitySerializer.class)
    @JsonDeserialize(using = IdentityDeserializer.class)
    private Long stateId;

    private CountryInfo country;

    private StateInfo state;

    public StateRegion() {
    }

    public StateRegion(Long stateId, CountryInfo country, StateInfo state) {
        this.stateId = stateId;
        this.country = country;
        this.state = state;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public CountryInfo getCountry() {
        return country;
    }

    public void setCountry(CountryInfo country) {
        this.country = country;
    }

    public StateInfo getState() {
        return state;
    }

    public void setState(StateInfo state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "StateRegion{" +
                "stateId=" + stateId +
                ", country=" + country +
                ", state=" + state +
                '}';
    }

}

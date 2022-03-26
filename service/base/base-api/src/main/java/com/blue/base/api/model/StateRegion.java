package com.blue.base.api.model;

import java.io.Serializable;

/**
 * state region
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class StateRegion implements Serializable {

    private static final long serialVersionUID = 8341453968629415940L;

    private CountryInfo country;

    private StateInfo state;

    public StateRegion() {
    }

    public StateRegion(CountryInfo country, StateInfo state) {
        this.country = country;
        this.state = state;
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
        return "AreaRegion{" +
                "country=" + country +
                ", state=" + state +
                '}';
    }

}

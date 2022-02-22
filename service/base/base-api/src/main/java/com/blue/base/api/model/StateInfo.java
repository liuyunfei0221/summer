package com.blue.base.api.model;

import java.io.Serializable;

/**
 * state info
 *
 * @author DarkBlue
 */
public final class StateInfo implements Serializable {

    private static final long serialVersionUID = 8273318922366078046L;

    private Long id;

    private String name;

    private String stateCode;

    public StateInfo() {
    }

    public StateInfo(Long id, String name, String stateCode) {
        this.id = id;
        this.name = name;
        this.stateCode = stateCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
                ", name='" + name + '\'' +
                ", stateCode='" + stateCode + '\'' +
                '}';
    }

}

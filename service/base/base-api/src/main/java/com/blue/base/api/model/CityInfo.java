package com.blue.base.api.model;

import java.io.Serializable;

/**
 * city info
 *
 * @author DarkBlue
 */
public final class CityInfo implements Serializable {

    private static final long serialVersionUID = -1328565587446491452L;

    private Long id;

    private String name;

    public CityInfo() {
    }

    public CityInfo(Long id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public String toString() {
        return "CityInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

}

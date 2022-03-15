package com.blue.base.api.model;

import java.io.Serializable;

/**
 * area info
 *
 * @author DarkBlue
 */
public final class AreaInfo implements Serializable {

    private static final long serialVersionUID = 6113836087899954124L;

    private Long id;

    private String name;

    public AreaInfo() {
    }

    public AreaInfo(Long id, String name) {
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
        return "AreaInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

}

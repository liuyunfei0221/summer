package com.blue.base.api.model;

import java.io.Serializable;

/**
 * dict info
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class DictInfo implements Serializable {

    private static final long serialVersionUID = 945497603248391168L;

    private Long id;

    private String name;

    private String value;

    public DictInfo() {
    }

    public DictInfo(Long id, String name, String value) {
        this.id = id;
        this.name = name;
        this.value = value;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "DictInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

}

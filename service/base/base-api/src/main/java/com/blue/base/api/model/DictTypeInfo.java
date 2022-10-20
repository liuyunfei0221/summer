package com.blue.base.api.model;

import java.io.Serializable;

/**
 * dict type info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class DictTypeInfo implements Serializable {

    private static final long serialVersionUID = 737243208720221581L;

    private Long id;

    private String code;

    private String name;

    public DictTypeInfo() {
    }

    public DictTypeInfo(Long id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DictTypeInfo{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

}

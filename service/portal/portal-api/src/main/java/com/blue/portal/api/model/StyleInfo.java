package com.blue.portal.api.model;

import java.io.Serializable;


/**
 * style api info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class StyleInfo implements Serializable {

    private static final long serialVersionUID = -6705469024881953415L;

    private Long id;

    private String name;

    private String attributes;

    private Integer type;

    /**
     * @see com.blue.base.constant.common.Status
     */
    private Integer status;

    public StyleInfo() {
    }

    public StyleInfo(Long id, String name, String attributes, Integer type, Integer status) {
        this.id = id;
        this.name = name;
        this.attributes = attributes;
        this.type = type;
        this.status = status;
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

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "StyleInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", attributes='" + attributes + '\'' +
                ", type=" + type +
                ", status=" + status +
                '}';
    }

}

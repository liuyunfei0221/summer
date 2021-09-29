package com.blue.secure.api.model;

import java.io.Serializable;

/**
 * role info for rest
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class RoleInfo implements Serializable {

    private static final long serialVersionUID = -5082010699818859757L;

    /**
     * id
     */
    private Long id;

    /**
     * role name
     */
    private String name;

    /**
     * role disc
     */
    private String description;

    /**
     * default role?
     */
    private Boolean isDefault;

    public RoleInfo() {
    }

    public RoleInfo(Long id, String name, String description, Boolean isDefault) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isDefault = isDefault;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    @Override
    public String toString() {
        return "RoleInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isDefault=" + isDefault +
                '}';
    }

}

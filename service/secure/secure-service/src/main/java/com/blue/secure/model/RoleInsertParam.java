package com.blue.secure.model;

import java.io.Serializable;

/**
 * params for insert a new role
 *
 * @author liuyunfei
 * @date 2021/11/1
 * @apiNote
 */
@SuppressWarnings("unused")
public class RoleInsertParam implements Serializable {

    private static final long serialVersionUID = -4099297009366670202L;

    private String name;

    private String description;

    public RoleInsertParam() {
    }

    public RoleInsertParam(String name, String description) {
        this.name = name;
        this.description = description;
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

    @Override
    public String toString() {
        return "RoleInsertParam{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}

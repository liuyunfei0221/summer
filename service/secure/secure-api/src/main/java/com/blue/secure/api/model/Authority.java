package com.blue.secure.api.model;

import java.io.Serializable;
import java.util.List;

/**
 * api层权限信息封装
 *
 * @author DarkBlue
 */
public final class Authority implements Serializable {

    private static final long serialVersionUID = -6642630151271066692L;

    private RoleInfo role;

    private List<ResourceInfo> resources;

    public Authority(RoleInfo role, List<ResourceInfo> resources) {
        this.role = role;
        this.resources = resources;
    }

    public RoleInfo getRole() {
        return role;
    }

    public void setRole(RoleInfo role) {
        this.role = role;
    }

    public List<ResourceInfo> getResources() {
        return resources;
    }

    public void setResources(List<ResourceInfo> resources) {
        this.resources = resources;
    }

    @Override
    public String toString() {
        return "Authority{" +
                "role=" + role +
                ", resources=" + resources +
                '}';
    }

}

package com.blue.auth.api.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Authority base on role
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class MemberAuthority implements Serializable {

    private static final long serialVersionUID = 7280942272560851900L;

    /**
     * roles info
     */
    private List<RoleInfo> roles;

    /**
     * resource info
     */
    private Set<ResourceInfo> resources;

    public MemberAuthority() {
    }

    public MemberAuthority(List<RoleInfo> roles, Set<ResourceInfo> resources) {
        this.roles = roles;
        this.resources = resources;
    }

    public List<RoleInfo> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleInfo> roles) {
        this.roles = roles;
    }

    public Set<ResourceInfo> getResources() {
        return resources;
    }

    public void setResources(Set<ResourceInfo> resources) {
        this.resources = resources;
    }

    @Override
    public String toString() {
        return "MemberAuthority{" +
                "roles=" + roles +
                ", resources=" + resources +
                '}';
    }

}

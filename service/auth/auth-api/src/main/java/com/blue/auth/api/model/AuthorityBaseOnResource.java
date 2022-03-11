package com.blue.auth.api.model;

import java.io.Serializable;
import java.util.List;

/**
 * Authority base on resource
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class AuthorityBaseOnResource implements Serializable {

    private static final long serialVersionUID = 1065291753547924787L;
    
    /**
     * resource info
     */
    private ResourceInfo resource;

    /**
     * roles infos
     */
    private List<RoleInfo> roles;

    public AuthorityBaseOnResource() {
    }

    public AuthorityBaseOnResource(ResourceInfo resource, List<RoleInfo> roles) {
        this.resource = resource;
        this.roles = roles;
    }

    public ResourceInfo getResource() {
        return resource;
    }

    public void setResource(ResourceInfo resource) {
        this.resource = resource;
    }

    public List<RoleInfo> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleInfo> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "ResourceBaseAuthority{" +
                "resource=" + resource +
                ", roles=" + roles +
                '}';
    }

}

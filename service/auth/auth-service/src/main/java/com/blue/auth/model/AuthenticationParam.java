package com.blue.auth.model;

import java.io.Serializable;

/**
 * Authentication Param
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class AuthenticationParam implements Serializable {

    private static final long serialVersionUID = -2968145715086465331L;

    private String authentication;

    public AuthenticationParam() {
    }

    public AuthenticationParam(String authentication) {
        this.authentication = authentication;
    }

    public String getAuthentication() {
        return authentication;
    }

    public void setAuthentication(String authentication) {
        this.authentication = authentication;
    }

    @Override
    public String toString() {
        return "AuthenticationParam{" +
                "authentication='" + authentication + '\'' +
                '}';
    }

}

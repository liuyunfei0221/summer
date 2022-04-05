package com.blue.auth.api.model;

import java.io.Serializable;

/**
 * auth assert params
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class AccessAssert implements Serializable {

    private static final long serialVersionUID = -7162502253332663234L;

    /**
     * authentication
     */
    private String authentication;

    /**
     * request method str
     */
    private String method;

    /**
     * uri str
     */
    private String uri;

    /**
     * It is only provided for serialization and is not recommended.
     * The correctness of parameters cannot be guaranteed based on setter
     */
    @Deprecated
    public AccessAssert() {
    }

    public AccessAssert(String authentication, String method, String uri) {
        this.authentication = authentication;
        this.method = method;
        this.uri = uri;
    }

    public String getAuthentication() {
        return authentication;
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    @Override
    public String toString() {
        return "AssertAuth{" +
                "authentication='" + authentication + '\'' +
                ", method='" + method + '\'' +
                ", uri='" + uri + '\'' +
                '}';
    }

}

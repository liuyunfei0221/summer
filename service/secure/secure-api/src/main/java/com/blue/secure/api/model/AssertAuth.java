package com.blue.secure.api.model;

import java.io.Serializable;

/**
 * auth assert params
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class AssertAuth implements Serializable {

    private static final long serialVersionUID = -7162502253332663234L;

    /**
     * authentication
     */
    private String authentication;

    /**
     * client ip
     */
    private String ip;

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
    public AssertAuth() {
    }

    public AssertAuth(String authentication, String ip, String method, String uri) {
        this.authentication = authentication;
        this.ip = ip;
        this.method = method;
        this.uri = uri;
    }

    public String getAuthentication() {
        return authentication;
    }

    public String getIp() {
        return ip;
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
                ", ip='" + ip + '\'' +
                ", method='" + method + '\'' +
                ", uri='" + uri + '\'' +
                '}';
    }

}

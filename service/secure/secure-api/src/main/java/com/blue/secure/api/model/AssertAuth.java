package com.blue.secure.api.model;

import java.io.Serializable;

/**
 * 认证信息封装类
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class AssertAuth implements Serializable {

    private static final long serialVersionUID = -7162502253332663234L;

    /**
     * 认证信息
     */
    private String authentication;

    /**
     * client ip地址
     */
    private String ip;

    /**
     * 请求方式/大写
     */
    private String method;

    /**
     * 资源路径
     */
    private String uri;

    /**
     * 仅提供用于序列化,不推荐使用,基于set无法保证参数正确性
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

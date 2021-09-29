package com.blue.secure.api.model;

import java.io.Serializable;

/**
 * member auth with sec key used for wechat mini pro
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces", "GrazieInspection"})
public final class MemberAuth implements Serializable {

    private static final long serialVersionUID = -726710353190335045L;

    /**
     * jwtAuth
     */
    private final String auth;

    /**
     * sec key
     */
    private final String secKey;

    public MemberAuth(String auth, String secKey) {
        if (auth == null || "".equals(auth))
            throw new RuntimeException("auth不能为空或''");
        if (secKey == null || "".equals(secKey))
            throw new RuntimeException("secKey不能为空或''");

        this.auth = auth;
        this.secKey = secKey;
    }

    public String getAuth() {
        return auth;
    }

    public String getSecKey() {
        return secKey;
    }

    @Override
    public String toString() {
        return "MemberAuthInfo{" +
                "auth='" + auth + '\'' +
                ", secKey='" + secKey + '\'' +
                '}';
    }

}

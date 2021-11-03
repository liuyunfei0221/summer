package com.blue.secure.model;

import java.io.Serializable;

import static com.blue.base.constant.base.CommonException.BAD_REQUEST_EXP;

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
            throw BAD_REQUEST_EXP.exp;
        if (secKey == null || "".equals(secKey))
            throw BAD_REQUEST_EXP.exp;

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

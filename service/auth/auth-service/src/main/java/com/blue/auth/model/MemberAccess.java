package com.blue.auth.model;

import com.blue.base.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;

/**
 * member auth with sec key used for access
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class MemberAccess implements Serializable {

    private static final long serialVersionUID = -1930250799048962030L;

    /**
     * jwtAuth
     */
    private final String auth;

    /**
     * sec key
     */
    private final String secKey;

    public MemberAccess(String auth, String secKey) {
        if (auth == null || "".equals(auth))
            throw new BlueException(BAD_REQUEST);
        if (secKey == null || "".equals(secKey))
            throw new BlueException(BAD_REQUEST);

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
        return "MemberAccess{" +
                "auth='" + auth + '\'' +
                ", secKey='" + secKey + '\'' +
                '}';
    }

}

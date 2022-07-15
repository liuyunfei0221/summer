package com.blue.auth.model;

import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isBlank;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;

/**
 * member auth with sec key and refresh token used for login
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class MemberAuth implements Serializable {

    private static final long serialVersionUID = -1624371887303174789L;

    /**
     * jwtAuth
     */
    private final String auth;

    /**
     * sec key
     */
    private final String secKey;

    /**
     * refresh token
     */
    private final String refresh;

    public MemberAuth(String auth, String secKey, String refresh) {
        if (isBlank(auth))
            throw new BlueException(BAD_REQUEST);
        if (isBlank(secKey))
            throw new BlueException(BAD_REQUEST);
        if (isBlank(refresh))
            throw new BlueException(BAD_REQUEST);

        this.auth = auth;
        this.secKey = secKey;
        this.refresh = refresh;
    }

    public String getAuth() {
        return auth;
    }

    public String getSecKey() {
        return secKey;
    }

    public String getRefresh() {
        return refresh;
    }

    @Override
    public String toString() {
        return "MemberAuth{" +
                "auth='" + auth + '\'' +
                ", secKey='" + secKey + '\'' +
                ", refresh='" + refresh + '\'' +
                '}';
    }

}

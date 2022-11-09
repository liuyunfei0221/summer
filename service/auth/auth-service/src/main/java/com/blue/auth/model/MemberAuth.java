package com.blue.auth.model;

import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isBlank;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;

/**
 * member auth with sec key and refresh token used for session
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class MemberAuth implements Serializable {

    private static final long serialVersionUID = -1624371887303174789L;

    /**
     * access/jwt
     */
    private final String access;

    /**
     * sec key
     */
    private final String secKey;

    /**
     * refresh token
     */
    private final String refresh;

    public MemberAuth(String access, String secKey, String refresh) {
        if (isBlank(access))
            throw new BlueException(BAD_REQUEST);
        if (isBlank(secKey))
            throw new BlueException(BAD_REQUEST);
        if (isBlank(refresh))
            throw new BlueException(BAD_REQUEST);

        this.access = access;
        this.secKey = secKey;
        this.refresh = refresh;
    }

    public String getAccess() {
        return access;
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
                "access='" + access + '\'' +
                ", secKey='" + secKey + '\'' +
                ", refresh='" + refresh + '\'' +
                '}';
    }

}

package com.blue.auth.model;

import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isBlank;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;

/**
 * member auth with sec key used for access
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class MemberAccess implements Serializable {

    private static final long serialVersionUID = -1930250799048962030L;

    /**
     * access/jwt
     */
    private final String access;

    /**
     * sec key
     */
    private final String secKey;

    public MemberAccess(String access, String secKey) {
        if (isBlank(access))
            throw new BlueException(BAD_REQUEST);
        if (isBlank(secKey))
            throw new BlueException(BAD_REQUEST);

        this.access = access;
        this.secKey = secKey;
    }

    public String getAccess() {
        return access;
    }

    public String getSecKey() {
        return secKey;
    }

    @Override
    public String toString() {
        return "MemberAccess{" +
                "access='" + access + '\'' +
                ", secKey='" + secKey + '\'' +
                '}';
    }

}

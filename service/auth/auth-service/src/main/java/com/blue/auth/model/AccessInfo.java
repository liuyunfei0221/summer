package com.blue.auth.model;

import com.blue.base.model.exps.BlueException;

import java.io.Serializable;
import java.util.List;

import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.constant.common.ResponseElement.BAD_REQUEST;

/**
 * member access info/json str in redis cache
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class AccessInfo implements Serializable {

    private static final long serialVersionUID = -4570565004686061550L;

    /**
     * json web token
     */
    private String jwt;

    /**
     * role id
     */
    private List<Long> roleIds;

    /**
     * public key used for encrypt rest
     */
    private String pubKey;

    public AccessInfo() {
    }

    public AccessInfo(String jwt, List<Long> roleIds, String pubKey) {
        if (isBlank(jwt))
            throw new BlueException(BAD_REQUEST);
        if (isInvalidIdentities(roleIds))
            throw new BlueException(BAD_REQUEST);
        if (isBlank(pubKey))
            throw new BlueException(BAD_REQUEST);

        this.jwt = jwt;
        this.roleIds = roleIds;
        this.pubKey = pubKey;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        if (isBlank(jwt))
            throw new BlueException(BAD_REQUEST);

        this.jwt = jwt;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        if (isInvalidIdentities(roleIds))
            throw new BlueException(BAD_REQUEST);

        this.roleIds = roleIds;
    }

    public String getPubKey() {
        return pubKey;
    }

    public void setPubKey(String pubKey) {
        if (isBlank(pubKey))
            throw new BlueException(BAD_REQUEST);

        this.pubKey = pubKey;
    }

    @Override
    public String toString() {
        return "AccessInfo{" +
                "jwt='" + jwt + '\'' +
                ", roleIds=" + roleIds +
                ", pubKey='" + pubKey + '\'' +
                '}';
    }

}

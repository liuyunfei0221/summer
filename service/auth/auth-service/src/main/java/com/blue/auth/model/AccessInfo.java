package com.blue.auth.model;

import com.blue.base.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.base.common.base.BlueChecker.isBlank;
import static com.blue.base.common.base.BlueChecker.isNull;
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
    private Long roleId;

    /**
     * public key used for encrypt rest
     */
    private String pubKey;

    public AccessInfo() {
    }

    public AccessInfo(String jwt, Long roleId, String pubKey) {
        if (isBlank(jwt))
            throw new BlueException(BAD_REQUEST);
        if (isNull(roleId) || roleId < 1L)
            throw new BlueException(BAD_REQUEST);
        if (isBlank(pubKey))
            throw new BlueException(BAD_REQUEST);

        this.jwt = jwt;
        this.roleId = roleId;
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

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        if (isNull(roleId) || roleId < 1L)
            throw new BlueException(BAD_REQUEST);

        this.roleId = roleId;
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
                ", roleId=" + roleId +
                ", pubKey='" + pubKey + '\'' +
                '}';
    }

}

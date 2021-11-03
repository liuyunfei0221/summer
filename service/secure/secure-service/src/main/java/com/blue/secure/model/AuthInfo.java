package com.blue.secure.model;

import java.io.Serializable;

import static com.blue.base.constant.base.CommonException.BAD_REQUEST_EXP;

/**
 * member auth info/json str in redis cache
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class AuthInfo implements Serializable {

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

    public AuthInfo() {
    }

    public AuthInfo(String jwt, Long roleId, String pubKey) {
        if (jwt == null || "".equals(jwt))
            throw BAD_REQUEST_EXP.exp;
        if (roleId == null || roleId < 1L)
            throw BAD_REQUEST_EXP.exp;
        if (pubKey == null || "".equals(pubKey))
            throw BAD_REQUEST_EXP.exp;

        this.jwt = jwt;
        this.roleId = roleId;
        this.pubKey = pubKey;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        if (jwt == null || "".equals(jwt))
            throw BAD_REQUEST_EXP.exp;

        this.jwt = jwt;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        if (roleId == null || roleId < 1L)
            throw BAD_REQUEST_EXP.exp;

        this.roleId = roleId;
    }

    public String getPubKey() {
        return pubKey;
    }

    public void setPubKey(String pubKey) {
        if (pubKey == null || "".equals(pubKey))
            throw BAD_REQUEST_EXP.exp;

        this.pubKey = pubKey;
    }

    @Override
    public String toString() {
        return "AuthInfo{" +
                "jwt='" + jwt + '\'' +
                ", roleId=" + roleId +
                ", pubKey='" + pubKey + '\'' +
                '}';
    }

}

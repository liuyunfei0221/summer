package com.blue.secure.model;

import java.io.Serializable;

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
            throw new RuntimeException("jwt cant be null or ''");
        if (roleId == null || roleId < 1L)
            throw new RuntimeException("roleId cant be null or less than 1");
        if (pubKey == null || "".equals(pubKey))
            throw new RuntimeException("pubKey cant be null or ''");

        this.jwt = jwt;
        this.roleId = roleId;
        this.pubKey = pubKey;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        if (jwt == null || "".equals(jwt))
            throw new RuntimeException("jwt cant be null or ''");

        this.jwt = jwt;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        if (roleId == null || roleId < 1L)
            throw new RuntimeException("roleId cant be null or less than 1");

        this.roleId = roleId;
    }

    public String getPubKey() {
        return pubKey;
    }

    public void setPubKey(String pubKey) {
        if (pubKey == null || "".equals(pubKey))
            throw new RuntimeException("pubKey cant be null or ''");

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

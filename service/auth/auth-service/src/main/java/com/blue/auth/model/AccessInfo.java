package com.blue.auth.model;

import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;
import java.util.List;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;

/**
 * member access info/json str in redis cache
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class AccessInfo implements Serializable {

    private static final long serialVersionUID = -4570565004686061550L;

    /**
     * random gamma
     */
    private String gamma;

    /**
     * member id
     */
    private Long id;

    /**
     * role id
     */
    private List<Long> roleIds;

    /**
     * public key used for encrypt rest
     */
    private String pubKey;

    /**
     * session time stamp
     */
    private Long loginMillisTimeStamp;

    public AccessInfo() {
    }

    public AccessInfo(String gamma, Long id, List<Long> roleIds, String pubKey, Long loginMillisTimeStamp) {
        if (isBlank(gamma))
            throw new BlueException(BAD_REQUEST);
        if (isInvalidIdentity(id))
            throw new BlueException(BAD_REQUEST);
        if (isInvalidIdentities(roleIds))
            throw new BlueException(BAD_REQUEST);
        if (isBlank(pubKey))
            throw new BlueException(BAD_REQUEST);
        if (isNull(loginMillisTimeStamp) || loginMillisTimeStamp <= 0L)
            throw new BlueException(BAD_REQUEST);

        this.gamma = gamma;
        this.id = id;
        this.roleIds = roleIds;
        this.pubKey = pubKey;
        this.loginMillisTimeStamp = loginMillisTimeStamp;
    }

    public String getGamma() {
        return gamma;
    }

    public void setGamma(String gamma) {
        if (isBlank(gamma))
            throw new BlueException(BAD_REQUEST);

        this.gamma = gamma;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        if (isInvalidIdentity(id))
            throw new BlueException(BAD_REQUEST);

        this.id = id;
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

    public Long getLoginMillisTimeStamp() {
        return loginMillisTimeStamp;
    }

    public void setLoginMillisTimeStamp(Long loginMillisTimeStamp) {
        this.loginMillisTimeStamp = loginMillisTimeStamp;
    }

    @Override
    public String toString() {
        return "AccessInfo{" +
                "gamma='" + gamma + '\'' +
                ", id=" + id +
                ", roleIds=" + roleIds +
                ", pubKey='" + pubKey + '\'' +
                ", loginMillisTimeStamp=" + loginMillisTimeStamp +
                '}';
    }

}

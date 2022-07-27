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
     * role id
     */
    private List<Long> roleIds;

    /**
     * public key used for encrypt rest
     */
    private String pubKey;

    /**
     * login time stamp
     */
    private Long loginMillisTimeStamp;

    public AccessInfo() {
    }

    public AccessInfo(String gamma, List<Long> roleIds, String pubKey, Long loginMillisTimeStamp) {
        if (isBlank(gamma))
            throw new BlueException(BAD_REQUEST);
        if (isInvalidIdentities(roleIds))
            throw new BlueException(BAD_REQUEST);
        if (isBlank(pubKey))
            throw new BlueException(BAD_REQUEST);
        if (isNull(loginMillisTimeStamp))
            throw new BlueException(BAD_REQUEST);

        this.gamma = gamma;
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
                ", roleIds=" + roleIds +
                ", pubKey='" + pubKey + '\'' +
                ", loginMillisTimeStamp=" + loginMillisTimeStamp +
                '}';
    }

}

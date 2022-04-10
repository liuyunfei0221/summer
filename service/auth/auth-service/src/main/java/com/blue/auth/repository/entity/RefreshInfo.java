package com.blue.auth.repository.entity;

import com.blue.base.model.exps.BlueException;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;

/**
 * refresh token info entity
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "unused"})
public class RefreshInfo implements Serializable {

    private static final long serialVersionUID = -2732456864958923965L;

    /**
     * refresh keyId used for pk
     */
    @Id
    private String id;

    /**
     * random gamma
     */
    private String gamma;

    /**
     * member id
     */
    private String memberId;

    /**
     * login type
     */
    private String loginType;

    /**
     * device type
     */
    private String deviceType;

    /**
     * login time stamp
     */
    private String loginTime;

    public RefreshInfo() {
    }

    public RefreshInfo(String id, String gamma, String memberId, String loginType, String deviceType, String loginTime) {
        if (id == null || "".equals(id))
            throw new BlueException(BAD_REQUEST);
        if (gamma == null || "".equals(gamma))
            throw new BlueException(BAD_REQUEST);
        if (memberId == null || "".equals(memberId))
            throw new BlueException(BAD_REQUEST);
        if (loginType == null || "".equals(loginType))
            throw new BlueException(BAD_REQUEST);
        if (deviceType == null || "".equals(deviceType))
            throw new BlueException(BAD_REQUEST);
        if (loginTime == null || "".equals(loginTime))
            throw new BlueException(BAD_REQUEST);

        this.id = id;
        this.gamma = gamma;
        this.memberId = memberId;
        this.loginType = loginType;
        this.deviceType = deviceType;
        this.loginTime = loginTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGamma() {
        return gamma;
    }

    public void setGamma(String gamma) {
        this.gamma = gamma;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    @Override
    public String toString() {
        return "RefreshInfo{" +
                "id='" + id + '\'' +
                ", gamma='" + gamma + '\'' +
                ", memberId='" + memberId + '\'' +
                ", loginType='" + loginType + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", loginTime='" + loginTime + '\'' +
                '}';
    }

}
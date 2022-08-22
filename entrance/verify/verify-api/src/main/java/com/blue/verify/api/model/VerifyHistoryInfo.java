package com.blue.verify.api.model;

import com.blue.basic.constant.verify.VerifyBusinessType;
import com.blue.basic.serializer.IdentityDeserializer;
import com.blue.basic.serializer.IdentitySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * verify history info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class VerifyHistoryInfo implements Serializable {

    private static final long serialVersionUID = -3829756530290630049L;

    @JsonSerialize(using = IdentitySerializer.class)
    @JsonDeserialize(using = IdentityDeserializer.class)
    private Long id;

    /**
     * @see com.blue.basic.constant.verify.VerifyType
     */
    private String verifyType;

    /**
     * @see VerifyBusinessType
     */
    private String businessType;

    private String destination;

    private String verify;

    private String requestIp;

    @JsonSerialize(using = IdentitySerializer.class)
    @JsonDeserialize(using = IdentityDeserializer.class)
    private Long createTime;

    public VerifyHistoryInfo() {
    }

    public VerifyHistoryInfo(Long id, String verifyType, String businessType, String destination, String verify, String requestIp, Long createTime) {
        this.id = id;
        this.verifyType = verifyType;
        this.businessType = businessType;
        this.destination = destination;
        this.verify = verify;
        this.requestIp = requestIp;
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVerifyType() {
        return verifyType;
    }

    public void setVerifyType(String verifyType) {
        this.verifyType = verifyType;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    public String getRequestIp() {
        return requestIp;
    }

    public void setRequestIp(String requestIp) {
        this.requestIp = requestIp;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "VerifyHistoryInfo{" +
                "id=" + id +
                ", verifyType='" + verifyType + '\'' +
                ", businessType='" + businessType + '\'' +
                ", destination='" + destination + '\'' +
                ", verify='" + verify + '\'' +
                ", requestIp='" + requestIp + '\'' +
                ", createTime=" + createTime +
                '}';
    }

}

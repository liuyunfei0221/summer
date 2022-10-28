package com.blue.verify.repository.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * verify history
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
@Document(collection = "verifyHistory")
public final class VerifyHistory implements Serializable {

    private static final long serialVersionUID = -2430618760264577569L;

    @Id
    private Long id;

    /**
     * @see com.blue.basic.constant.verify.VerifyType
     */
    private String verifyType;

    /**
     * @see com.blue.basic.constant.verify.VerifyBusinessType
     */
    private String businessType;

    private String destination;

    private String verify;

    private String requestIp;

    private Long createTime;

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
        return "VerifyHistory{" +
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

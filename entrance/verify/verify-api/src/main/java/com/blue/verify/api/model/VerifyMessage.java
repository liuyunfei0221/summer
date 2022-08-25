package com.blue.verify.api.model;

import com.blue.basic.constant.verify.VerifyBusinessType;

import java.io.Serializable;

/**
 * verify message
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class VerifyMessage implements Serializable {

    private static final long serialVersionUID = 2100414210141423667L;

    /**
     * @see com.blue.basic.constant.verify.VerifyType
     */
    private String verifyType;

    /**
     * @see VerifyBusinessType
     */
    private String businessType;

    /**
     * if verifyType is IMAGE, destination will be ignored.
     * if verifyType is SMS, destination should be a phone num.
     * if verifyType is MAIL, destination should be an email address.
     */
    private String destination;

    private String verify;

    public VerifyMessage() {
    }

    public VerifyMessage(String verifyType, String businessType, String destination, String verify) {
        this.verifyType = verifyType;
        this.businessType = businessType;
        this.destination = destination;
        this.verify = verify;
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

    @Override
    public String toString() {
        return "VerifyMessage{" +
                "verifyType='" + verifyType + '\'' +
                ", businessType='" + businessType + '\'' +
                ", destination='" + destination + '\'' +
                ", verify='" + verify + '\'' +
                '}';
    }

}

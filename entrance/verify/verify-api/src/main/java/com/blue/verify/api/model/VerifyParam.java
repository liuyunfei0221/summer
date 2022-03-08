package com.blue.verify.api.model;

import java.io.Serializable;

/**
 * marketing event
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class VerifyParam implements Serializable {

    private static final long serialVersionUID = -4952556683651447255L;

    /**
     * @see com.blue.base.constant.verify.VerifyType
     */
    private String verifyType;

    /**
     * @see com.blue.base.constant.verify.VerifyBusinessType
     */
    private String businessType;

    /**
     * if verifyType is IMAGE, destination will be ignored.
     * if verifyType is SMS, destination should be a phone num.
     * if verifyType is MAIL, destination should be an email address.
     */
    private String destination;

    public VerifyParam() {
    }

    public VerifyParam(String verifyType, String businessType, String destination) {
        this.verifyType = verifyType;
        this.businessType = businessType;
        this.destination = destination;
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

    @Override
    public String toString() {
        return "VerifyParam{" +
                "verifyType='" + verifyType + '\'' +
                ", businessType='" + businessType + '\'' +
                ", destination='" + destination + '\'' +
                '}';
    }

}

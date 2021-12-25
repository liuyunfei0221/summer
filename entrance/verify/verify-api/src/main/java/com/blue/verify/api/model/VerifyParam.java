package com.blue.verify.api.model;

import com.blue.base.constant.verify.VerifyType;

import java.io.Serializable;

/**
 * marketing event
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class VerifyParam implements Serializable {

    private static final long serialVersionUID = -4952556683651447255L;

    private VerifyType verifyType;

    private String destination;

    public VerifyParam() {
    }

    public VerifyParam(VerifyType verifyType, String destination) {
        this.verifyType = verifyType;
        this.destination = destination;
    }

    public VerifyType getVerifyType() {
        return verifyType;
    }

    public void setVerifyType(VerifyType verifyType) {
        this.verifyType = verifyType;
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
                "verifyType=" + verifyType +
                ", destination='" + destination + '\'' +
                '}';
    }

}

package com.blue.captcha.api.model;

import com.blue.base.constant.captcha.CaptchaType;

import java.io.Serializable;

/**
 * marketing event
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class CaptchaParam implements Serializable {

    private static final long serialVersionUID = -4952556683651447255L;

    private CaptchaType captchaType;

    private String destination;

    public CaptchaParam() {
    }

    public CaptchaParam(CaptchaType captchaType, String destination) {
        this.captchaType = captchaType;
        this.destination = destination;
    }

    public CaptchaType getCaptchaType() {
        return captchaType;
    }

    public void setCaptchaType(CaptchaType captchaType) {
        this.captchaType = captchaType;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @Override
    public String toString() {
        return "CaptchaParam{" +
                "captchaType=" + captchaType +
                ", destination='" + destination + '\'' +
                '}';
    }

}

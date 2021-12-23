package com.blue.captcha.api.model;

import java.io.Serializable;

/**
 * marketing event
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class CaptchaPair implements Serializable {

    private static final long serialVersionUID = 8206003773691021548L;

    private String key;

    private String verify;

    public CaptchaPair() {
    }

    public CaptchaPair(String key, String verify) {
        this.key = key;
        this.verify = verify;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    @Override
    public String toString() {
        return "CaptchaPair{" +
                "key='" + key + '\'' +
                ", verify='" + verify + '\'' +
                '}';
    }

}

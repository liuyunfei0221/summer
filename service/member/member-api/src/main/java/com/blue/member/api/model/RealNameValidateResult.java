package com.blue.member.api.model;

import java.io.Serializable;

/**
 * real name validate result
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "JavaDoc"})
public final class RealNameValidateResult implements Serializable {

    private static final long serialVersionUID = -8138342920321935826L;

    private boolean allow;

    private String message;

    public RealNameValidateResult() {
    }

    public RealNameValidateResult(boolean allow, String message) {
        this.allow = allow;
        this.message = message;
    }

    public boolean isAllow() {
        return allow;
    }

    public void setAllow(boolean allow) {
        this.allow = allow;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "RealNameValidateResult{" +
                "allow=" + allow +
                ", message='" + message + '\'' +
                '}';
    }

}

package com.blue.jwt.constant;

/**
 * Exception information constraint / try to be unified with ResponseElement in common
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public enum JwtResponseElement {

    /**
     * UNAUTHORIZED
     */
    UNAUTHORIZED(401, 401, "Authentication failed or expired, please log in"),

    /**
     * INTERNAL_SERVER_ERROR
     */
    INTERNAL_SERVER_ERROR(203, 203, "System busy");

    /**
     * http status
     */
    public final int status;

    /**
     * business code
     */
    public final int code;

    /**
     * message
     */
    public final String message;

    JwtResponseElement(int status, int code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

}
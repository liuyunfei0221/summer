package com.blue.jwt.constant;

/**
 * 异常信息约束/与common中的ResponseElemen尽量统一
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public enum JwtResponseElement {

    /**
     * 未认证
     */
    UNAUTHORIZED(401, 401, "认证失败或过期,请重新登录"),

    /**
     * 系统错误
     */
    INTERNAL_SERVER_ERROR(203, 203, "服务开小差了");

    /**
     * 异常状态码
     */
    public final int status;

    /**
     * 异常业务码
     */
    public final int code;

    /**
     * 异常信息
     */
    public final String message;

    JwtResponseElement(int status, int code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

}
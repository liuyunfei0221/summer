package com.blue.base.constant.base;

import org.springframework.http.HttpStatus;

/**
 * 异常信息约束
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public enum ResponseElement {

    /**
     * 成功
     */
    OK(HttpStatus.OK.value(), HttpStatus.OK.value(), "成功"),

    /**
     * 请求过多
     */
    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS.value(), HttpStatus.TOO_MANY_REQUESTS.value(), "请求过多"),

    /**
     * 降级
     */
    PARTIAL_CONTENT(HttpStatus.PARTIAL_CONTENT.value(), HttpStatus.PARTIAL_CONTENT.value(), "降级"),

    /**
     * 未认证
     */
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.value(), "认证失败或过期,请重新登录"),

    /**
     * 权限不足
     */
    FORBIDDEN(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.value(), "权限不足"),

    /**
     * 资源不存在
     */
    NOT_FOUND(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.value(), "资源不存在"),

    /**
     * 参数错误
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.value(), "参数错误"),

    /**
     * "媒体类型错误"
     */
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), "媒体类型错误"),

    /**
     * 请求内容过大
     */
    PAYLOAD_TOO_LARGE(HttpStatus.PAYLOAD_TOO_LARGE.value(), HttpStatus.PAYLOAD_TOO_LARGE.value(), "内容过大或请求/响应头过大"),

    /**
     * 数据不存在
     */
    NO_CONTENT(HttpStatus.NO_CONTENT.value(), HttpStatus.NO_CONTENT.value(), "数据不存在"),

    /**
     * 风控拦截
     */
    NOT_ACCEPTABLE(HttpStatus.NOT_ACCEPTABLE.value(), HttpStatus.NOT_ACCEPTABLE.value(), "您已被限制访问"),

    /**
     * 系统错误
     */
    INTERNAL_SERVER_ERROR(HttpStatus.NON_AUTHORITATIVE_INFORMATION.value(), HttpStatus.NON_AUTHORITATIVE_INFORMATION.value(), "服务开小差了"),

    /**
     * 请求超时
     */
    REQUEST_TIMEOUT(HttpStatus.REQUEST_TIMEOUT.value(), HttpStatus.REQUEST_TIMEOUT.value(), "请求超时"),

    /**
     * 网关超时
     */
    GATEWAY_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT.value(), HttpStatus.GATEWAY_TIMEOUT.value(), "网关超时");

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

    ResponseElement(int status, int code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

}
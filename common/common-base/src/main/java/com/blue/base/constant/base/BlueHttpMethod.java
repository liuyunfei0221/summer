package com.blue.base.constant.base;

import org.springframework.http.HttpMethod;

/**
 * http methods
 *
 * @author liuyunfei
 */
public enum BlueHttpMethod {

    /**
     * GET
     */
    GET(HttpMethod.GET.name().toUpperCase(), HttpMethod.GET),

    /**
     * HEAD
     */
    HEAD(HttpMethod.HEAD.name().toUpperCase(), HttpMethod.HEAD),

    /**
     * POST
     */
    POST(HttpMethod.POST.name().toUpperCase(), HttpMethod.POST),

    /**
     * PUT
     */
    PUT(HttpMethod.PUT.name().toUpperCase(), HttpMethod.PUT),

    /**
     * PATCH
     */
    PATCH(HttpMethod.PATCH.name().toUpperCase(), HttpMethod.PATCH),

    /**
     * DELETE
     */
    DELETE(HttpMethod.DELETE.name().toUpperCase(), HttpMethod.DELETE),

    /**
     * OPTIONS
     */
    OPTIONS(HttpMethod.OPTIONS.name().toUpperCase(), HttpMethod.OPTIONS),

    /**
     * TRACE
     */
    TRACE(HttpMethod.TRACE.name().toUpperCase(), HttpMethod.TRACE);

    public final String value;

    public final HttpMethod method;

    BlueHttpMethod(String value, HttpMethod method) {
        this.value = value;
        this.method = method;
    }

}

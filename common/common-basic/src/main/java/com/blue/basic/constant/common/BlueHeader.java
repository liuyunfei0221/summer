package com.blue.basic.constant.common;

import org.springframework.http.HttpHeaders;

/**
 * HTTP headers
 *
 * @author liuyunfei
 */
public enum BlueHeader {

    /**
     * auth
     */
    AUTHORIZATION(HttpHeaders.AUTHORIZATION),

    /**
     * private key for client
     */
    SECRET("Secret"),

    /**
     * refresh token
     */
    REFRESH("Refresh"),

    /**
     * user agent
     */
    USER_AGENT(HttpHeaders.USER_AGENT),

    /**
     * request ip
     */
    REQUEST_IP("Request_Ip"),

    /**
     * metadata
     */
    METADATA("Metadata"),

    /**
     * member source
     */
    SOURCE("Source"),

    /**
     * host
     */
    HOST(HttpHeaders.HOST),

    /**
     * verify key
     */
    VERIFY_KEY("Verify-Key"),

    /**
     * verify value
     */
    VERIFY_VALUE("Verify-Value"),

    /**
     * need to pass turing test
     */
    NEED_TURING_TEST("Need-Turing-Test"),

    /**
     * turing data
     */
    TURING_DATA("Turing-Data"),

    /**
     * REQ EXTRA
     */
    REQUEST_EXTRA("RequestExtra"),

    /**
     * RES EXTRA
     */
    RESPONSE_EXTRA("ResponseExtra"),

    /**
     * X_FORWARDED_FOR
     */
    X_FORWARDED_FOR("X-Forwarded-For"),

    /**
     * PROXY_CLIENT_IP
     */
    PROXY_CLIENT_IP("Proxy-Client-IP"),

    /**
     * WL_PROXY_CLIENT_IP
     */
    WL_PROXY_CLIENT_IP("WL-Proxy-Client-IP"),

    /**
     * HTTP_CLIENT_IP
     */
    HTTP_CLIENT_IP("HTTP_CLIENT_IP"),

    /**
     * HTTP_X_FORWARDED_FOR
     */
    HTTP_X_FORWARDED_FOR("HTTP_X_FORWARDED_FOR"),

    /**
     * X_REAL_IP
     */
    X_REAL_IP("X-Real-IP");

    public final String name;

    BlueHeader(String name) {
        this.name = name;
    }
}

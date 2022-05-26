package com.blue.base.constant.base;

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
     * content
     */
    CONTENT_DISPOSITION(HttpHeaders.CONTENT_DISPOSITION),

    /**
     * host
     */
    HOST(HttpHeaders.HOST),

    /**
     * EXTRA
     */
    EXTRA("Extra");

    public final String name;

    BlueHeader(String name) {
        this.name = name;
    }
}

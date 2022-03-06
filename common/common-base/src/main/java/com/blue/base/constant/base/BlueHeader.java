package com.blue.base.constant.base;

import org.springframework.http.HttpHeaders;

/**
 * HTTP headers
 *
 * @author DarkBlue
 */
public enum BlueHeader {

    /**
     * auth
     */
    AUTHORIZATION(HttpHeaders.AUTHORIZATION),

    /**
     * USER_AGENT
     */
    USER_AGENT(HttpHeaders.USER_AGENT),

    /**
     * metadata
     */
    METADATA("Metadata"),

    /**
     * private key for client
     */
    SECRET("Secret"),

    /**
     * verify key
     */
    VERIFY_KEY("Verify-Key"),

    /**
     * verify value
     */
    VERIFY_VALUE("Verify-Value"),

    /**
     * content
     */
    CONTENT_DISPOSITION("Content-disposition");

    public final String name;

    BlueHeader(String name) {
        this.name = name;
    }
}

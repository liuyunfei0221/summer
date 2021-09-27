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
     * private key for client
     */
    SECRET("Secret"),

    /**
     * metadata
     */
    METADATA("Metadata"),

    /**
     * content
     */
    CONTENT_DISPOSITION("Content-disposition");

    public final String name;


    BlueHeader(String name) {
        this.name = name;
    }
}

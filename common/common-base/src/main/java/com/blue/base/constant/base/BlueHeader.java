package com.blue.base.constant.base;

import org.springframework.http.HttpHeaders;

/**
 * HTTP头信息
 *
 * @author DarkBlue
 */
public enum BlueHeader {

    /**
     * 认证信息
     */
    AUTHORIZATION(HttpHeaders.AUTHORIZATION),

    /**
     * 签发给客户端的私钥
     */
    SECRET("Secret"),

    /**
     * 元数据
     */
    METADATA("Metadata");

    public final String name;


    BlueHeader(String name) {
        this.name = name;
    }
}

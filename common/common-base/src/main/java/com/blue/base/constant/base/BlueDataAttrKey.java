package com.blue.base.constant.base;

/**
 * attr keys
 *
 * @author DarkBlue
 */
public enum BlueDataAttrKey {

    /**
     * 请求方法
     */
    METHOD("method", "请求方法"),

    /**
     * 资源路径
     */
    URI("uri", "资源路径"),

    /**
     * 请求内容
     */
    REQUEST_BODY("requestBody", "请求内容"),

    /**
     * 响应状态码
     */
    RESPONSE_STATUS("responseStatus", "响应状态码"),

    /**
     * 响应内容
     */
    RESPONSE_BODY("responseBody", "响应内容"),

    /**
     * 用于标识不同请求的requestId
     */
    REQUEST_ID("requestId", "请求id"),

    /**
     * 元数据
     */
    METADATA("metadata", "元数据"),

    /**
     * 未解析的JWT
     */
    JWT("jwt", "jwt"),

    /**
     * 认证信息
     */
    ACCESS("access", "认证信息"),

    /**
     * 客户端ip地址
     */
    CLIENT_IP("clientIp", "客户端ip"),

    /**
     * 公钥/String
     */
    SEC_KEY("secKey", "成员公钥"),

    /**
     * 是否请求不解密/boolean
     */
    PRE_UN_DECRYPTION("preUnDecryption", "是否为无需解密的请求的资源"),

    /**
     * 是否响应不加密/boolean
     */
    POST_UN_ENCRYPTION("postUnEncryption", "是否为无需加密的响应的资源"),

    /**
     * 是否包含请求体
     */
    EXISTENCE_REQUEST_BODY("existenceRequestBody", "是否为包含请求体的资源"),

    /**
     * 是否包含响应体
     */
    EXISTENCE_RESPONSE_BODY("existenceResponseBody", "是否为包含响应体的资源");

    /**
     * key
     */
    public final String key;

    public final String disc;

    BlueDataAttrKey(String key, String disc) {
        this.key = key;
        this.disc = disc;
    }

}

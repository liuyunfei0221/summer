package com.blue.base.constant.base;

/**
 * attr keys
 *
 * @author DarkBlue
 */
public enum BlueDataAttrKey {

    /**
     * request method
     */
    METHOD("method", "request method"),

    /**
     * resource uri
     */
    URI("uri", "resource uri"),

    /**
     * request body
     */
    REQUEST_BODY("requestBody", "request body"),

    /**
     * response status code
     */
    RESPONSE_STATUS("responseStatus", "response status code"),

    /**
     * response body
     */
    RESPONSE_BODY("responseBody", "response body"),

    /**
     * request id
     */
    REQUEST_ID("requestId", "request id"),

    /**
     * request metadata
     */
    METADATA("metadata", "request metadata"),

    /**
     * original json web token
     */
    JWT("jwt", "original json web token"),

    /**
     * access from jwt with parse
     */
    ACCESS("access", "access from jwt with parse"),

    /**
     * client ip
     */
    CLIENT_IP("clientIp", "client ip"),

    /**
     * public key on server
     */
    SEC_KEY("secKey", "public key on server"),

    /**
     * Whether it is a requested resource that does not need to be decrypted
     */
    PRE_UN_DECRYPTION("preUnDecryption", "Whether it is a requested resource that does not need to be decrypted"),

    /**
     * Whether it is a resource that does not need to be encrypted
     */
    POST_UN_ENCRYPTION("postUnEncryption", "Whether it is a resource that does not need to be encrypted"),

    /**
     * Whether it is a resource containing the request body
     */
    EXISTENCE_REQUEST_BODY("existenceRequestBody", "Whether it is a resource containing the request body"),

    /**
     * Whether it is a resource containing the response body
     */
    EXISTENCE_RESPONSE_BODY("existenceResponseBody", "Whether it is a resource containing the response body");

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

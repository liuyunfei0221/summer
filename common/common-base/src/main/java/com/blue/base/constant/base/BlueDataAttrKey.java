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
    METHOD("method"),

    /**
     * resource uri
     */
    URI("uri"),

    /**
     * request body
     */
    REQUEST_BODY("requestBody"),

    /**
     * response status code
     */
    RESPONSE_STATUS("responseStatus"),

    /**
     * response body
     */
    RESPONSE_BODY("responseBody"),

    /**
     * request id
     */
    REQUEST_ID("requestId"),

    /**
     * request metadata
     */
    METADATA("metadata"),

    /**
     * original json web token
     */
    JWT("jwt"),

    /**
     * access from jwt with parse
     */
    ACCESS("access"),

    /**
     * client ip
     */
    CLIENT_IP("clientIp"),

    /**
     * public key on server
     */
    SEC_KEY("secKey"),

    /**
     * Whether it is a requested resource that does not need to be decrypted
     */
    PRE_UN_DECRYPTION("preUnDecryption"),

    /**
     * Whether it is a resource that does not need to be encrypted
     */
    POST_UN_ENCRYPTION("postUnEncryption"),

    /**
     * Whether it is a resource containing the request body
     */
    EXISTENCE_REQUEST_BODY("existenceRequestBody"),

    /**
     * Whether it is a resource containing the response body
     */
    EXISTENCE_RESPONSE_BODY("existenceResponseBody");

    /**
     * key
     */
    public final String key;

    BlueDataAttrKey(String key) {
        this.key = key;
    }

}

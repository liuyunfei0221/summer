package com.blue.base.constant.common;

/**
 * attr keys
 *
 * @author liuyunfei
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
     * real uri
     */
    REAL_URI("realUri"),

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
     * public key on server
     */
    SEC_KEY("secKey"),

    /**
     * access from jwt with parse
     */
    ACCESS("access"),

    /**
     * member id in access(id)
     */
    MEMBER_ID("memberId"),

    /**
     * role id in access(roleId)
     */
    ROLE_ID("roleId"),

    /**
     * credential type in access(credentialType)
     */
    CREDENTIAL_TYPE("credentialType"),

    /**
     * device type in access(deviceType)
     */
    DEVICE_TYPE("deviceType"),

    /**
     * login time in access(loginTime)
     */
    LOGIN_TIME("loginTime"),

    /**
     * client ip
     */
    CLIENT_IP("clientIp"),

    /**
     * host
     */
    HOST("host"),

    /**
     * request user agent
     */
    USER_AGENT("userAgent"),

    /**
     * Whether it is a requested resource that does not need to be decrypted
     */
    REQUEST_UN_DECRYPTION("requestUnDecryption"),

    /**
     * Whether it is a resource that does not need to be encrypted
     */
    RESPONSE_UN_ENCRYPTION("responseUnEncryption"),

    /**
     * Whether it is a resource containing the request body
     */
    EXISTENCE_REQUEST_BODY("existenceRequestBody"),

    /**
     * Whether it is a resource containing the response body
     */
    EXISTENCE_RESPONSE_BODY("existenceResponseBody"),

    /**
     * duration seconds/used for stop over
     */
    DURATION_SECONDS("durationSeconds"),

    /**
     * do not need turing test?
     */
    WITHOUT_TURING_TEST("withoutTuringTest");

    /**
     * key
     */
    public final String key;

    BlueDataAttrKey(String key) {
        this.key = key;
    }

}

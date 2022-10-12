package com.blue.auth.constant;

/**
 * session attr keys
 *
 * @author liuyunfei
 */
public enum LoginAttribute {
    
    //for session by client
    /**
     * account/phone/email for session by client
     */
    IDENTITY("identity"),

    /**
     * password/message verify for session by client
     */
    ACCESS("access"),

    //for session by mini pro
    /**
     * user encrypted data for session by mini pro
     */
    ENCRYPTED_DATA("encryptedData"),

    /**
     * encrypt algorithm iv for session by mini pro
     */
    IV("iv"),

    /**
     * js code for session by mini pro
     */
    JS_CODE("jsCode");

    public final String key;

    LoginAttribute(String key) {
        this.key = key;
    }

}

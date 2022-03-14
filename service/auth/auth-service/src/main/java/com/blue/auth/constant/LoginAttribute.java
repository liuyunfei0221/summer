package com.blue.auth.constant;

/**
 * login attr keys
 *
 * @author liuyunfei
 * @date 2021/10/21
 * @apiNote
 */
public enum LoginAttribute {

    //common
    /**
     * verify code if trigger risk control
     */
    VERIFICATION_CODE("verificationCode"),

    //for login by client
    /**
     * account/phone/email for login by client
     */
    IDENTITY("identity"),

    /**
     * password/message verify for login by client
     */
    ACCESS("access"),

    //for login by mini pro
    /**
     * user encrypted data for login by mini pro
     */
    ENCRYPTED_DATA("encryptedData"),

    /**
     * encrypt algorithm iv for login by mini pro
     */
    IV("iv"),

    /**
     * js code for login by mini pro
     */
    JS_CODE("jsCode");

    public final String key;

    LoginAttribute(String key) {
        this.key = key;
    }

}

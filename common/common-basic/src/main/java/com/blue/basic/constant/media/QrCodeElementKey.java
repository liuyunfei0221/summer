package com.blue.basic.constant.media;

/**
 * qr code element key
 *
 * @author liuyunfei
 */
public enum QrCodeElementKey {

    /**
     * default key
     */
    DEFAULT("DEFAULT"),

    /**
     * email
     */
    EMAIL("EMAIL"),

    /**
     * role
     */
    ROLE("ROLE"),

    /**
     * roles
     */
    ROLES("ROLES");

    public final String key;

    QrCodeElementKey(String key) {
        this.key = key;
    }

}

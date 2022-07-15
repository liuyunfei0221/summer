package com.blue.basic.constant.media;


/**
 * qr code generator type
 *
 * @author liuyunfei
 */
public enum QrCodeGenType {

    /**
     * user info
     */
    USER_INFO(1, "user info");

    public final int identity;

    public final String disc;

    QrCodeGenType(int identity, String disc) {
        this.identity = identity;
        this.disc = disc;
    }

}

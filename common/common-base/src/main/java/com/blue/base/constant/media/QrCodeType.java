package com.blue.base.constant.media;


/**
 * qr code type
 *
 * @author liuyunfei
 */
public enum QrCodeType {

    /**
     * user info
     */
    USER_INFO(1, "user info");

    public final int identity;

    public final String disc;

    QrCodeType(int identity, String disc) {
        this.identity = identity;
        this.disc = disc;
    }

}

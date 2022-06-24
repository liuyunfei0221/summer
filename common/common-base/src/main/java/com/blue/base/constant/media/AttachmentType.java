package com.blue.base.constant.media;


/**
 * attachment type
 *
 * @author liuyunfei
 */
public enum AttachmentType {

    /**
     * common
     */
    COMMON(1, "common attachment"),

    /**
     * icon
     */
    ICON(2, "member's icon"),

    /**
     * qrcode
     */
    QR_CODE(3, "member's qrcode");

    public final int identity;

    public final String disc;

    AttachmentType(int identity, String disc) {
        this.identity = identity;
        this.disc = disc;
    }

}

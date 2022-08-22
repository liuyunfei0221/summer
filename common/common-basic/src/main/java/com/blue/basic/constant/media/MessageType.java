package com.blue.basic.constant.media;

/**
 * message type
 *
 * @author liuyunfei
 */
public enum MessageType {

    /**
     * sms message
     */
    SMS(1, "sms"),

    /**
     * mail message
     */
    MAIL(2, "mail"),

    /**
     * device push message
     */
    DEVICE_PUSH(3, "push");

    public final int identity;

    public final String disc;

    MessageType(int identity, String disc) {
        this.identity = identity;
        this.disc = disc;
    }
}

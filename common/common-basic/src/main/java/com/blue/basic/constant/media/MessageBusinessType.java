package com.blue.basic.constant.media;

/**
 * message business type
 *
 * @author liuyunfei
 */
public enum MessageBusinessType {

    /**
     * message
     */
    T1(1, "t1"),

    /**
     * message
     */
    T2(2, "t2"),

    /**
     * message
     */
    T3(3, "t3");

    public final int identity;

    public final String disc;

    MessageBusinessType(int identity, String disc) {
        this.identity = identity;
        this.disc = disc;
    }

}

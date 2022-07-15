package com.blue.basic.constant.portal;

/**
 * style type
 *
 * @author liuyunfei
 */
public enum StyleType {

    /**
     * popular
     */
    POPULAR(1, "popular"),

    /**
     * newest
     */
    NEWEST(2, "newest"),

    /**
     * recommend
     */
    RECOMMEND(3, "recommend");


    /**
     * identity
     */
    public final int identity;

    /**
     * disc
     */
    public final String disc;

    StyleType(int identity, String disc) {
        this.identity = identity;
        this.disc = disc;
    }

}

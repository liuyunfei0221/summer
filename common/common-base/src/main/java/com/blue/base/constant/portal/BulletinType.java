package com.blue.base.constant.portal;

/**
 * bulletin type
 *
 * @author DarkBlue
 */
public enum BulletinType {

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

    BulletinType(int identity, String disc) {
        this.identity = identity;
        this.disc = disc;
    }

}

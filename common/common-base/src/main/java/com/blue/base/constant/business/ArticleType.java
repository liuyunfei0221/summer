package com.blue.base.constant.business;

/**
 * article types
 *
 * @author DarkBlue
 */
@SuppressWarnings("GrazieInspection")
public enum ArticleType {

    //Article type 1.TRICKED 2.RECOMMEND 3.SHITS 4.GRUMBLE 5.SHARE 6.WATERING

    /**
     * tricked
     */
    TRICKED(1, "tricked"),

    /**
     * recommendation
     */
    RECOMMENDATION(2, "recommendation"),

    /**
     * shits
     */
    SHITS(3, "shits"),

    /**
     * grumble
     */
    GRUMBLE(4, "grumble"),

    /**
     * share
     */
    SHARE(5, "share"),

    /**
     * water
     */
    WATERING(6, "water");


    /**
     * identity
     */
    public final int identity;

    /**
     * disc
     */
    public final String disc;

    ArticleType(int identity, String disc) {
        this.identity = identity;
        this.disc = disc;
    }
}

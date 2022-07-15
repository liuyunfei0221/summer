package com.blue.basic.constant.member;


/**
 * gender
 *
 * @author liuyunfei
 */
public enum Gender {

    /**
     * male
     */
    MALE(1, "male"),

    /**
     * female
     */
    FEMALE(0, "female"),

    /**
     * other
     */
    OTHER(2, "other"),

    /**
     * unknown
     */
    UNKNOWN(3, "unknown");

    /**
     * identity
     */
    public final int identity;

    /**
     * disc
     */
    public final String disc;

    Gender(int identity, String disc) {
        this.identity = identity;
        this.disc = disc;
    }

}

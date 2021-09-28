package com.blue.base.constant.member;


/**
 * gender
 *
 * @author DarkBlue
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
    OTHER(2, "other");

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

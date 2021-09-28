package com.blue.base.constant.business;

/**
 * subject type
 *
 * @author DarkBlue
 */
public enum SubjectType {

    /**
     * 文章
     */
    ARTICLE(1, "article");

    /**
     * identity
     */
    public int identity;

    /**
     * disc
     */
    public String disc;

    SubjectType(int identity, String disc) {
        this.identity = identity;
        this.disc = disc;
    }

}

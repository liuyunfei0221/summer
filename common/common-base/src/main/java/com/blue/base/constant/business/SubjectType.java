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
    ARTICLE(1, "article"),

    /**
     * COMMENT
     */
    COMMENT(2, "comment"),

    /**
     * LINK
     */
    LINK(3, "link"),

    /**
     * REPLY
     */
    REPLY(4, "reply");

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

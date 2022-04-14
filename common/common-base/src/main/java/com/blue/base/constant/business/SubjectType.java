package com.blue.base.constant.business;

/**
 * subject type
 *
 * @author liuyunfei
 */
public enum SubjectType {

    /**
     * ARTICLE
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
    public final int identity;

    /**
     * disc
     */
    public final String disc;

    SubjectType(int identity, String disc) {
        this.identity = identity;
        this.disc = disc;
    }

}

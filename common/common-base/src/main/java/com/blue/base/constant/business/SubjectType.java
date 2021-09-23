package com.blue.base.constant.business;

/**
 * 内容类型
 *
 * @author DarkBlue
 */
public enum SubjectType {

    /**
     * 文章
     */
    ARTICLE(1, "文章");

    /**
     * 类型标识
     */
    public int identity;

    /**
     * 类型描述
     */
    public String disc;

    SubjectType(int identity, String disc) {
        this.identity = identity;
        this.disc = disc;
    }

}

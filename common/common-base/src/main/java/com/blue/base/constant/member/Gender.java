package com.blue.base.constant.member;


/**
 * 性别
 *
 * @author DarkBlue
 */
public enum Gender {

    /**
     * 男性
     */
    MALE(1, "男"),

    /**
     * 女性
     */
    FEMALE(0, "女"),

    /**
     * 其他
     */
    OTHER(2, "其他");

    /**
     * 数字标识
     */
    public final int identity;

    /**
     * 描述
     */
    public final String disc;

    Gender(int identity, String disc) {
        this.identity = identity;
        this.disc = disc;
    }

}

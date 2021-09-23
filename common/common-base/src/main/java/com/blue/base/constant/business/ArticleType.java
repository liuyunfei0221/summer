package com.blue.base.constant.business;

/**
 * 文章类型
 *
 * @author DarkBlue
 */
public enum ArticleType {

    //文章类型 1爬坑经验 2推荐 3吐槽 4牢骚 5晒物 6水文

    /**
     * 爬坑经验
     */
    TRICKED(1, "爬坑经验"),

    /**
     * 推荐
     */
    RECOMMEND(2, "推荐"),

    /**
     * 吐槽
     */
    SHITS(3, "吐槽"),

    /**
     * 牢骚
     */
    GRUMBLE(4, "牢骚"),

    /**
     * 晒物
     */
    SHARE(5, "晒物"),

    /**
     * 水文
     */
    WATERING(6, "水文");


    /**
     * 类型标识
     */
    public int identity;

    /**
     * 类型描述
     */
    public String disc;

    ArticleType(int identity, String disc) {
        this.identity = identity;
        this.disc = disc;
    }
}

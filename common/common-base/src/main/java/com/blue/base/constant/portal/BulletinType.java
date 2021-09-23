package com.blue.base.constant.portal;

/**
 * 公告类型
 *
 * @author DarkBlue
 */
public enum BulletinType {

    /**
     * 热门公告
     */
    POPULAR(1, "热门公告"),

    /**
     * 最新公告
     */
    NEWEST(2, "最新公告"),

    /**
     * 推荐公告
     */
    RECOMMEND(3, "推荐公告");


    /**
     * 类型标识
     */
    public int identity;

    /**
     * 类型描述
     */
    public String disc;

    BulletinType(int identity, String disc) {
        this.identity = identity;
        this.disc = disc;
    }

}

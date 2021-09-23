package com.blue.base.constant.data;

/**
 * 统计类型
 *
 * @author liuyunfei
 * @date 2021/9/3
 * @apiNote
 */
public enum StatisticsType {

    /**
     * 统计当日
     */
    MEMBER_ACTIVE("MA", "统计活跃用户");

    public final String identity;

    public final String disc;

    StatisticsType(String identity, String disc) {
        this.identity = identity;
        this.disc = disc;
    }
}

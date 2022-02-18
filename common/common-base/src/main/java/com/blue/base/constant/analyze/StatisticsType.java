package com.blue.base.constant.analyze;

/**
 * statistics type
 *
 * @author liuyunfei
 * @date 2021/9/3
 * @apiNote
 */
public enum StatisticsType {

    /**
     * for member active
     */
    MA("MA", "for member active");

    /**
     * identity
     */
    public final String identity;

    /**
     * disc
     */
    public final String disc;

    StatisticsType(String identity, String disc) {
        this.identity = identity;
        this.disc = disc;
    }
}

package com.blue.identity.constant;

/**
 * Snowflake configuration
 *
 * @author DarkBlue
 */
public enum SnowflakeBits {

    /**
     * Second-level timestamp, length 33, maximum support 272 years, based on 2021/4/19 12:00 as the baseline, it can be used for 221 years
     * 秒级时间戳,长度33,最大支持272年,基于2021/4/19 12:00为基准线可使用221年
     */
    TIME_STAMP(33),

    /**
     * Data center ID, length 7, supports up to 128 data centers
     * 数据中心ID,长度7,最多支持128数据中心
     */
    DATA_CENTER(7),

    /**
     * Machine ID, length 8, each data center supports up to 256 machines
     * 机器ID,长度8,每数据中心最多支持256台机器
     */
    WORKER(8),

    /**
     * Self-increasing sequence, length 15, single machine single second 32768, can apply to the future time
     * 自增序列,长度15,单机单秒32768,可向未来时间申请
     */
    SEQUENCE(15);

    /**
     * len
     */
    public final int len;

    SnowflakeBits(int len) {
        this.len = len;
    }
}

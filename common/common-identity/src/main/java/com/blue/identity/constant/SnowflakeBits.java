package com.blue.identity.constant;

/**
 * Snowflake configuration
 *
 * @author DarkBlue
 */
public enum SnowflakeBits {

    /**
     * Second-level timestamp, length 33, maximum support 272 years, based on 2021/4/19 12:00 as the baseline, it can be used for 221 years
     */
    TIME_STAMP(33),

    /**
     * Data center ID, length 7, supports up to 128 data centers
     */
    DATA_CENTER(7),

    /**
     * Machine ID, length 8, each data center supports up to 256 machines
     */
    WORKER(8),

    /**
     * Self-increasing sequence, length 15, single machine single second 32768, can apply to the future time
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

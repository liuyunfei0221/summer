package com.blue.base.model.redis;

import java.io.Serializable;
import java.time.temporal.ChronoUnit;

/**
 * redis key expire info
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "unused"})
public final class KeyExpireParam implements Serializable {

    private static final long serialVersionUID = 713277204676056312L;

    /**
     * redis key
     */
    private String key;

    /**
     * expire time
     */
    private Long expire;

    /**
     * time unit
     */
    private ChronoUnit unit;

    public KeyExpireParam() {
    }

    public KeyExpireParam(String key, Long expire, ChronoUnit unit) {
        if (key == null || "".equals(key))
            throw new RuntimeException("key can't be blank");
        if (expire == null || expire < 1L)
            throw new RuntimeException("expire can't be null or less than 1");
        if (unit == null)
            throw new RuntimeException("unit can't be null");

        this.key = key;
        this.expire = expire;
        this.unit = unit;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        if (key == null || "".equals(key))
            throw new RuntimeException("key can't be blank");
        this.key = key;
    }

    public Long getExpire() {
        return expire;
    }

    public void setExpire(Long expire) {
        if (expire == null || expire < 1L)
            throw new RuntimeException("expire can't be null or less than 1");
        this.expire = expire;
    }

    public ChronoUnit getUnit() {
        return unit;
    }

    public void setUnit(ChronoUnit unit) {
        if (unit == null)
            throw new RuntimeException("unit can't be null");
        this.unit = unit;
    }

}

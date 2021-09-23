package com.blue.base.model.redis;

import java.io.Serializable;
import java.time.temporal.ChronoUnit;

/**
 * redis key过期时间数据封装
 *
 * @author DarkBlue
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
public final class KeyExpireParam implements Serializable {

    private static final long serialVersionUID = 713277204676056312L;

    /**
     * redis key
     */
    private String key;

    /**
     * 过期时间数值
     */
    private Long expire;

    /**
     * 过期时间单位
     */
    private ChronoUnit unit;

    public KeyExpireParam() {
    }

    public KeyExpireParam(String key, Long expire, ChronoUnit unit) {
        if (key == null || "".equals(key))
            throw new RuntimeException("key不能为空或''");
        if (expire == null || expire < 1L)
            throw new RuntimeException("过期时间不能为空或小于1");
        if (unit == null)
            throw new RuntimeException("过期单位不能为空或");

        this.key = key;
        this.expire = expire;
        this.unit = unit;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        if (key == null || "".equals(key))
            throw new RuntimeException("key不能为空或''");
        this.key = key;
    }

    public Long getExpire() {
        return expire;
    }

    public void setExpire(Long expire) {
        if (expire == null || expire < 1L)
            throw new RuntimeException("过期时间不能为空或小于1");
        this.expire = expire;
    }

    public ChronoUnit getUnit() {
        return unit;
    }

    public void setUnit(ChronoUnit unit) {
        if (unit == null)
            throw new RuntimeException("过期单位不能为空或");
        this.unit = unit;
    }

}

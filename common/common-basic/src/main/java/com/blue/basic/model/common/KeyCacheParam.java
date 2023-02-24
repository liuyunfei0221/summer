package com.blue.basic.model.common;

import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;
import java.time.temporal.ChronoUnit;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;

/**
 * redis key cache info
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "unused"})
public final class KeyCacheParam implements Serializable {

    private static final long serialVersionUID = -8897652918283866449L;

    /**
     * redis key
     */
    private String key;

    /**
     * redis key
     */
    private String value;

    /**
     * expire time
     */
    private Long expire;

    /**
     * time unit
     */
    private ChronoUnit unit;

    public KeyCacheParam() {
    }

    public KeyCacheParam(String key, String value, Long expire, ChronoUnit unit) {
        if (isBlank(key))
            throw new BlueException(BAD_REQUEST);
        if (isBlank(value))
            throw new BlueException(BAD_REQUEST);

        this.key = key;
        this.value = value;
        this.expire = expire;
        this.unit = unit;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        if (isBlank(key))
            throw new BlueException(BAD_REQUEST);

        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        if (isBlank(value))
            throw new BlueException(BAD_REQUEST);

        this.value = value;
    }

    public Long getExpire() {
        return expire;
    }

    public void setExpire(Long expire) {
        this.expire = expire;
    }

    public ChronoUnit getUnit() {
        return unit;
    }

    public void setUnit(ChronoUnit unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "KeyCacheParam{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", expire=" + expire +
                ", unit=" + unit +
                '}';
    }

}
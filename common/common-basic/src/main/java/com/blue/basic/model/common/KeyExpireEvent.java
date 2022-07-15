package com.blue.basic.model.common;

import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;
import java.time.temporal.ChronoUnit;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;

/**
 * redis key expire info
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "unused"})
public final class KeyExpireEvent implements Serializable {

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

    public KeyExpireEvent() {
    }

    public KeyExpireEvent(String key, Long expire, ChronoUnit unit) {
        if (isBlank(key))
            throw new BlueException(BAD_REQUEST);
        if (isInvalidIdentity(expire))
            throw new BlueException(BAD_REQUEST);
        if (isNull(unit))
            throw new BlueException(BAD_REQUEST);

        this.key = key;
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

    public Long getExpire() {
        return expire;
    }

    public void setExpire(Long expire) {
        if (isInvalidIdentity(expire))
            throw new BlueException(BAD_REQUEST);
        this.expire = expire;
    }

    public ChronoUnit getUnit() {
        return unit;
    }

    public void setUnit(ChronoUnit unit) {
        if (isNull(unit))
            throw new BlueException(BAD_REQUEST);
        this.unit = unit;
    }

}

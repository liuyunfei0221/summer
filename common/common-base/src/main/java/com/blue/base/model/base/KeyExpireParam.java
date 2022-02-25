package com.blue.base.model.base;

import com.blue.base.model.exps.BlueException;

import java.io.Serializable;
import java.time.temporal.ChronoUnit;

import static com.blue.base.common.base.BlueChecker.isBlank;
import static com.blue.base.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;

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
        if (isBlank(key))
            throw new BlueException(BAD_REQUEST);
        if (isInvalidIdentity(expire))
            throw new BlueException(BAD_REQUEST);
        if (unit == null)
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
        if (unit == null)
            throw new BlueException(BAD_REQUEST);
        this.unit = unit;
    }

}

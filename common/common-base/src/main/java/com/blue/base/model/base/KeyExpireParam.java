package com.blue.base.model.base;

import com.blue.base.model.exps.BlueException;

import java.io.Serializable;
import java.time.temporal.ChronoUnit;

import static com.blue.base.common.base.Asserter.isBlank;
import static com.blue.base.common.base.Asserter.isInvalidIdentity;
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
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, BAD_REQUEST.message, null);
        if (isInvalidIdentity(expire))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, BAD_REQUEST.message, null);
        if (unit == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, BAD_REQUEST.message, null);

        this.key = key;
        this.expire = expire;
        this.unit = unit;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        if (isBlank(key))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, BAD_REQUEST.message, null);
        this.key = key;
    }

    public Long getExpire() {
        return expire;
    }

    public void setExpire(Long expire) {
        if (isInvalidIdentity(expire))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, BAD_REQUEST.message, null);
        this.expire = expire;
    }

    public ChronoUnit getUnit() {
        return unit;
    }

    public void setUnit(ChronoUnit unit) {
        if (unit == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, BAD_REQUEST.message, null);
        this.unit = unit;
    }

}

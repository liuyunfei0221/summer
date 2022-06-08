package com.blue.redis.common;

import com.blue.base.model.exps.BlueException;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.lang.NonNull;

import static com.blue.base.common.base.BlueChecker.isBlank;
import static com.blue.base.common.base.BlueChecker.isNull;
import static com.blue.base.constant.common.ResponseElement.INTERNAL_SERVER_ERROR;
import static org.springframework.data.redis.core.script.DigestUtils.sha1DigestAsHex;

/**
 * redis script
 *
 * @author liuyunfei
 */
@SuppressWarnings({"FieldCanBeLocal", "AliControlFlowStatementWithoutBraces"})
public final class BlueRedisScript<T> implements RedisScript<T> {

    private final String SCRIPT;
    private final String SHA1;
    private final Class<T> TYPE;

    public BlueRedisScript(String script, Class<T> type) {
        if (isBlank(script) || isNull(type))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "script can't be blank, type can't be null");

        this.SCRIPT = script;
        this.SHA1 = sha1DigestAsHex(script);
        this.TYPE = type;
    }

    @Override
    public @NonNull
    String getSha1() {
        return SHA1;
    }

    @Override
    public Class<T> getResultType() {
        return TYPE;
    }

    @Override
    public @NonNull
    String getScriptAsString() {
        return SCRIPT;
    }

    @Override
    public boolean returnsRawValue() {
        return isNull(TYPE);
    }
}

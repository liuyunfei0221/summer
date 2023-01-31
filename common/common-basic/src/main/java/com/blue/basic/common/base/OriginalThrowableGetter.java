package com.blue.basic.common.base;

import com.blue.basic.model.exps.BlueException;

import static com.blue.basic.common.base.BlueChecker.isNotNull;
import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;

/**
 * original throwable getter
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "JavaDoc"})
public final class OriginalThrowableGetter {

    /**
     * get original throwable
     *
     * @param throwable
     * @return
     */
    public static Throwable getOriginalThrowable(Throwable throwable) {
        if (isNull(throwable))
            throw new BlueException(EMPTY_PARAM);

        Throwable original = throwable;
        Throwable cause;
        while (isNotNull(cause = original.getCause()))
            original = cause;

        return original;
    }

}
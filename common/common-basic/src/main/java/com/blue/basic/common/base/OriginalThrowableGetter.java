package com.blue.basic.common.base;

import static com.blue.basic.common.base.BlueChecker.isNotNull;
import static com.blue.basic.common.base.BlueChecker.isNull;

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
            throw new RuntimeException("throwable can't be null");

        Throwable original = throwable;
        Throwable cause;
        while (isNotNull(cause = original.getCause()))
            original = cause;

        return original;
    }

}

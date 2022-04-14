package com.blue.base.common.base;

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
        if (throwable == null)
            throw new RuntimeException("throwable can't be null");

        Throwable original = throwable;
        Throwable cause;
        while ((cause = original.getCause()) != null)
            original = cause;

        return original;
    }

}

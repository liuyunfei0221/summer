package com.blue.basic.component.exception.handler.inter;

import com.blue.basic.component.exception.model.common.ExceptionInfo;

/**
 * exp handler interface
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "JavaDoc"})
public interface ExceptionHandler {

    /**
     * exp name
     *
     * @return
     */
    String exceptionName();

    /**
     * handle exp
     *
     * @param throwable
     * @return
     */
    ExceptionInfo handle(Throwable throwable);

}

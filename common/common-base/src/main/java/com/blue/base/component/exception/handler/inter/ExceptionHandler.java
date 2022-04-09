package com.blue.base.component.exception.handler.inter;

import com.blue.base.component.exception.handler.model.ExceptionInfo;

/**
 * exp handler interface
 *
 * @author DarkBlue
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

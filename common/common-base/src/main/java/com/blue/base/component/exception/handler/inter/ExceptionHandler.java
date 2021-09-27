package com.blue.base.component.exception.handler.inter;

import com.blue.base.component.exception.handler.model.ExceptionHandleInfo;

/**
 * exp handler interface
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "JavaDoc", "UnnecessaryInterfaceModifier"})
public interface ExceptionHandler {

    /**
     * exp name
     *
     * @return
     */
    public String exceptionName();

    /**
     * handle exp
     *
     * @param throwable
     * @return
     */
    public ExceptionHandleInfo handle(Throwable throwable);

}

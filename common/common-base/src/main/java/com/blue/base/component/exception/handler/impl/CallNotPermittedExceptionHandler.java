package com.blue.base.component.exception.handler.impl;

import com.blue.base.component.exception.handler.inter.ExceptionHandler;
import com.blue.base.component.exception.handler.model.ExceptionInfo;
import reactor.util.Logger;

import static com.blue.base.constant.common.ResponseElement.INTERNAL_SERVER_ERROR;
import static reactor.util.Loggers.getLogger;

/**
 * resilience4j exp handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "SpellCheckingInspection"})
public final class CallNotPermittedExceptionHandler implements ExceptionHandler {

    private static final Logger LOGGER = getLogger(CallNotPermittedExceptionHandler.class);

    private static final String EXP_NAME = "io.github.resilience4j.circuitbreaker.CallNotPermittedException";

    private static final ExceptionInfo EXP_HANDLE_INFO = new ExceptionInfo(INTERNAL_SERVER_ERROR);

    @Override
    public String exceptionName() {
        return EXP_NAME;
    }

    @Override
    public ExceptionInfo handle(Throwable throwable) {
        LOGGER.info("callNotPermittedExceptionHandler -> handle(Throwable throwable), throwable = {0}", throwable);
        return EXP_HANDLE_INFO;
    }

}

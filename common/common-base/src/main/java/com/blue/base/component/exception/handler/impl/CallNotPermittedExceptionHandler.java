package com.blue.base.component.exception.handler.impl;

import com.blue.base.component.exception.handler.inter.ExceptionHandler;
import com.blue.base.component.exception.handler.model.ExceptionHandleInfo;
import com.blue.base.model.base.BlueResponse;
import reactor.util.Logger;

import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static reactor.util.Loggers.getLogger;

/**
 * resilience4j exp handler
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "SpellCheckingInspection"})
public final class CallNotPermittedExceptionHandler implements ExceptionHandler {

    private static final Logger LOGGER = getLogger(CallNotPermittedExceptionHandler.class);

    private static final String EXP_NAME = "io.github.resilience4j.circuitbreaker.CallNotPermittedException";

    private static final ExceptionHandleInfo EXP_HANDLE_INFO = new ExceptionHandleInfo(INTERNAL_SERVER_ERROR.status, new BlueResponse<>(INTERNAL_SERVER_ERROR.code, null, INTERNAL_SERVER_ERROR.message));

    @Override
    public String exceptionName() {
        return EXP_NAME;
    }

    @Override
    public ExceptionHandleInfo handle(Throwable throwable) {
        LOGGER.info("callNotPermittedExceptionHandler -> handle(Throwable throwable), throwable = {0}", throwable);
        return EXP_HANDLE_INFO;
    }

}

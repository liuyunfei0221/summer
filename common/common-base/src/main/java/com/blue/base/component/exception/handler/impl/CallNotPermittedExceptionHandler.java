package com.blue.base.component.exception.handler.impl;

import com.blue.base.component.exception.handler.inter.ExceptionHandler;
import com.blue.base.component.exception.handler.model.ExceptionHandleInfo;
import com.blue.base.model.base.BlueResult;
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

    @Override
    public String exceptionName() {
        return EXP_NAME;
    }

    @Override
    public ExceptionHandleInfo handle(Throwable throwable) {
        LOGGER.info("callNotPermittedExceptionHandler -> handle(Throwable throwable), throwable = {0}", throwable);
        return new ExceptionHandleInfo(INTERNAL_SERVER_ERROR.status, new BlueResult<>(INTERNAL_SERVER_ERROR.code, null, "服务繁忙,请刷新重试"));
    }

}

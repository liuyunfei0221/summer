package com.blue.base.component.exception.handler.impl;

import com.blue.base.component.exception.handler.inter.ExceptionHandler;
import com.blue.base.component.exception.handler.model.ExceptionHandleInfo;
import com.blue.base.model.base.BlueResult;
import reactor.util.Logger;

import static com.blue.base.constant.base.ResponseElement.GATEWAY_TIMEOUT;
import static reactor.util.Loggers.getLogger;

/**
 * 超时异常处理类
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused"})
public final class TimeoutExceptionHandler implements ExceptionHandler {

    private static final Logger LOGGER = getLogger(TimeoutExceptionHandler.class);

    private static final String EXP_NAME = "org.springframework.cloud.gateway.support.TimeoutException";

    @Override
    public String exceptionName() {
        return EXP_NAME;
    }

    @Override
    public ExceptionHandleInfo handle(Throwable throwable) {
        LOGGER.info("timeoutExceptionHandler -> handle(Throwable throwable), throwable = {0}", throwable);
        return new ExceptionHandleInfo(GATEWAY_TIMEOUT.status, new BlueResult<>(GATEWAY_TIMEOUT.code, null, GATEWAY_TIMEOUT.message));
    }
}

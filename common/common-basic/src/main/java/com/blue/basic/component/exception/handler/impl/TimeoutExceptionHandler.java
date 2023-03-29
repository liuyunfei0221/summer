package com.blue.basic.component.exception.handler.impl;

import com.blue.basic.component.exception.handler.inter.ExceptionHandler;
import com.blue.basic.component.exception.model.common.ExceptionInfo;
import org.slf4j.Logger;

import static com.blue.basic.constant.common.ResponseElement.GATEWAY_TIMEOUT;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * timeout exp handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused"})
public final class TimeoutExceptionHandler implements ExceptionHandler {

    private static final Logger LOGGER = getLogger(TimeoutExceptionHandler.class);

    private static final String EXP_NAME = "org.springframework.cloud.gateway.support.TimeoutException";

    private static final ExceptionInfo EXP_HANDLE_INFO = new ExceptionInfo(GATEWAY_TIMEOUT);

    @Override
    public String exceptionName() {
        return EXP_NAME;
    }

    @Override
    public ExceptionInfo handle(Throwable throwable) {
        LOGGER.info("timeoutExceptionHandler -> handle(Throwable throwable), throwable = {0}", throwable);
        return EXP_HANDLE_INFO;
    }

}
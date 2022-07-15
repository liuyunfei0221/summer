package com.blue.basic.component.exception.handler.impl;

import com.blue.basic.component.exception.handler.inter.ExceptionHandler;
import com.blue.basic.component.exception.model.ExceptionInfo;
import reactor.util.Logger;

import static com.blue.basic.constant.common.ResponseElement.REQUEST_TIMEOUT;
import static reactor.util.Loggers.getLogger;

/**
 * connection exp handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused"})
public final class ConnectExceptionHandler implements ExceptionHandler {

    private static final Logger LOGGER = getLogger(ConnectExceptionHandler.class);

    private static final String EXP_NAME = "java.net.ConnectException";

    private static final ExceptionInfo EXP_HANDLE_INFO = new ExceptionInfo(REQUEST_TIMEOUT);

    @Override
    public String exceptionName() {
        return EXP_NAME;
    }

    @Override
    public ExceptionInfo handle(Throwable throwable) {
        LOGGER.info("connectExceptionHandler -> handle(Throwable throwable), throwable = {0}", throwable);
        return EXP_HANDLE_INFO;
    }
}

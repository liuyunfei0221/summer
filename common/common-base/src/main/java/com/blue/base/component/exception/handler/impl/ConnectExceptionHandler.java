package com.blue.base.component.exception.handler.impl;

import com.blue.base.component.exception.handler.inter.ExceptionHandler;
import com.blue.base.component.exception.handler.model.ExceptionHandleInfo;
import com.blue.base.model.base.BlueResponse;
import reactor.util.Logger;

import static com.blue.base.constant.base.ResponseElement.REQUEST_TIMEOUT;
import static reactor.util.Loggers.getLogger;

/**
 * connection exp handler
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused"})
public final class ConnectExceptionHandler implements ExceptionHandler {

    private static final Logger LOGGER = getLogger(ConnectExceptionHandler.class);

    private static final String EXP_NAME = "java.net.ConnectException";

    private static final ExceptionHandleInfo EXP_HANDLE_INFO = new ExceptionHandleInfo(REQUEST_TIMEOUT.status, new BlueResponse<>(REQUEST_TIMEOUT.code, null, REQUEST_TIMEOUT.message));

    @Override
    public String exceptionName() {
        return EXP_NAME;
    }

    @Override
    public ExceptionHandleInfo handle(Throwable throwable) {
        LOGGER.info("connectExceptionHandler -> handle(Throwable throwable), throwable = {0}", throwable);
        return EXP_HANDLE_INFO;
    }
}

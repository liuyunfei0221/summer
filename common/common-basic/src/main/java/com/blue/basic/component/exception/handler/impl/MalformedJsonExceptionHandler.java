package com.blue.basic.component.exception.handler.impl;

import com.blue.basic.component.exception.handler.inter.ExceptionHandler;
import com.blue.basic.component.exception.handler.model.ExceptionInfo;
import reactor.util.Logger;

import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;
import static reactor.util.Loggers.getLogger;

/**
 * another json parse exp handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused"})
public final class MalformedJsonExceptionHandler implements ExceptionHandler {

    private static final Logger LOGGER = getLogger(MalformedJsonExceptionHandler.class);

    private static final String EXP_NAME = "com.google.gson.stream.MalformedJsonException";

    private static final ExceptionInfo EXP_HANDLE_INFO = new ExceptionInfo(BAD_REQUEST);

    @Override
    public String exceptionName() {
        return EXP_NAME;
    }

    @Override
    public ExceptionInfo handle(Throwable throwable) {
        LOGGER.info("malformedJsonExceptionHandler -> handle(Throwable throwable), throwable = {0}", throwable);
        return EXP_HANDLE_INFO;
    }

}

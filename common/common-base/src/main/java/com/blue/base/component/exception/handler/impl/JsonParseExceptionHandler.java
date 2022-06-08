package com.blue.base.component.exception.handler.impl;

import com.blue.base.component.exception.handler.inter.ExceptionHandler;
import com.blue.base.component.exception.handler.model.ExceptionInfo;
import reactor.util.Logger;

import static com.blue.base.constant.common.ResponseElement.BAD_REQUEST;
import static reactor.util.Loggers.getLogger;

/**
 * json parse exp handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused"})
public final class JsonParseExceptionHandler implements ExceptionHandler {

    private static final Logger LOGGER = getLogger(JsonParseExceptionHandler.class);

    private static final String EXP_NAME = "com.fasterxml.jackson.core.JsonParseException";

    private static final ExceptionInfo EXP_HANDLE_INFO = new ExceptionInfo(BAD_REQUEST);

    @Override
    public String exceptionName() {
        return EXP_NAME;
    }

    @Override
    public ExceptionInfo handle(Throwable throwable) {
        LOGGER.info("jsonParseExceptionHandler -> handle(Throwable throwable), throwable = {0}", throwable);
        return EXP_HANDLE_INFO;
    }
}

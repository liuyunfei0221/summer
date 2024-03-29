package com.blue.basic.component.exception.handler.impl;

import com.blue.basic.component.exception.handler.inter.ExceptionHandler;
import com.blue.basic.component.exception.model.common.ExceptionInfo;
import org.slf4j.Logger;

import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;
import static org.slf4j.LoggerFactory.getLogger;

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
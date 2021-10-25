package com.blue.base.component.exception.handler.impl;

import com.blue.base.component.exception.handler.inter.ExceptionHandler;
import com.blue.base.component.exception.handler.model.ExceptionHandleInfo;
import com.blue.base.model.base.BlueResponse;
import reactor.util.Logger;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static reactor.util.Loggers.getLogger;

/**
 * json parse exp handler
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused"})
public final class JsonParseExceptionHandler implements ExceptionHandler {

    private static final Logger LOGGER = getLogger(JsonParseExceptionHandler.class);

    private static final String EXP_NAME = "com.fasterxml.jackson.core.JsonParseException";

    @Override
    public String exceptionName() {
        return EXP_NAME;
    }

    @Override
    public ExceptionHandleInfo handle(Throwable throwable) {
        LOGGER.info("jsonParseExceptionHandler -> handle(Throwable throwable), throwable = {0}", throwable);
        return new ExceptionHandleInfo(BAD_REQUEST.status, new BlueResponse<>(BAD_REQUEST.code, null, "json data parsed failed"));
    }
}

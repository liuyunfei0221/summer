package com.blue.basic.component.exception.handler.impl;

import com.blue.basic.component.exception.handler.inter.ExceptionHandler;
import com.blue.basic.component.exception.model.common.ExceptionInfo;
import reactor.util.Logger;

import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;
import static reactor.util.Loggers.getLogger;

/**
 * mismatched input exp handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused"})
public final class MismatchedInputExceptionHandler implements ExceptionHandler {

    private static final Logger LOGGER = getLogger(MismatchedInputExceptionHandler.class);

    private static final String EXP_NAME = "com.fasterxml.jackson.databind.exc.MismatchedInputException";

    private static final ExceptionInfo EXP_HANDLE_INFO = new ExceptionInfo(BAD_REQUEST);

    @Override
    public String exceptionName() {
        return EXP_NAME;
    }

    @Override
    public ExceptionInfo handle(Throwable throwable) {
        LOGGER.info("mismatchedInputExceptionHandler -> handle(Throwable throwable), throwable = {0}", throwable);
        return EXP_HANDLE_INFO;
    }

}
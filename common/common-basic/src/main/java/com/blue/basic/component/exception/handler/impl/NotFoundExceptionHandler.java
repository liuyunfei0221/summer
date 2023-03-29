package com.blue.basic.component.exception.handler.impl;

import com.blue.basic.component.exception.handler.inter.ExceptionHandler;
import com.blue.basic.component.exception.model.common.ExceptionInfo;
import org.slf4j.Logger;

import static com.blue.basic.constant.common.ResponseElement.NOT_FOUND;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * 404 exp handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused"})
public final class NotFoundExceptionHandler implements ExceptionHandler {

    private static final Logger LOGGER = getLogger(NotFoundExceptionHandler.class);

    private static final String EXP_NAME = "org.springframework.cloud.gateway.support.NotFoundException";

    private static final ExceptionInfo EXP_HANDLE_INFO = new ExceptionInfo(NOT_FOUND);

    @Override
    public String exceptionName() {
        return EXP_NAME;
    }

    @Override
    public ExceptionInfo handle(Throwable throwable) {
        LOGGER.info("notFoundExceptionHandler -> handle(Throwable throwable), throwable = {0}", throwable);
        return EXP_HANDLE_INFO;
    }

}
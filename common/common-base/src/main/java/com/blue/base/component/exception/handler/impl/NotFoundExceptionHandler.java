package com.blue.base.component.exception.handler.impl;

import com.blue.base.component.exception.handler.inter.ExceptionHandler;
import com.blue.base.component.exception.handler.model.ExceptionHandleInfo;
import com.blue.base.model.base.BlueResult;
import reactor.util.Logger;

import static com.blue.base.constant.base.ResponseElement.NOT_FOUND;
import static reactor.util.Loggers.getLogger;

/**
 * 404 exp handler
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused"})
public final class NotFoundExceptionHandler implements ExceptionHandler {

    private static final Logger LOGGER = getLogger(NotFoundExceptionHandler.class);

    private static final String EXP_NAME = "org.springframework.cloud.gateway.support.NotFoundException";

    @Override
    public String exceptionName() {
        return EXP_NAME;
    }

    @Override
    public ExceptionHandleInfo handle(Throwable throwable) {
        LOGGER.info("notFoundExceptionHandler -> handle(Throwable throwable), throwable = {0}", throwable);
        return new ExceptionHandleInfo(NOT_FOUND.status, new BlueResult<>(NOT_FOUND.code, null, NOT_FOUND.message));
    }
}

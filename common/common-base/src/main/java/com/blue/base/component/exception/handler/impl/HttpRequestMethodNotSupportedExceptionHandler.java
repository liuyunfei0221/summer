package com.blue.base.component.exception.handler.impl;

import com.blue.base.component.exception.handler.inter.ExceptionHandler;
import com.blue.base.component.exception.handler.model.ExceptionHandleInfo;
import com.blue.base.model.base.BlueResult;
import reactor.util.Logger;

import static com.blue.base.constant.base.ResponseElement.NOT_FOUND;
import static reactor.util.Loggers.getLogger;

/**
 * method not support exp handler
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused"})
public final class HttpRequestMethodNotSupportedExceptionHandler implements ExceptionHandler {

    private static final Logger LOGGER = getLogger(HttpRequestMethodNotSupportedExceptionHandler.class);

    private static final String EXP_NAME = "org.springframework.web.HttpRequestMethodNotSupportedException";

    @Override
    public String exceptionName() {
        return EXP_NAME;
    }

    @Override
    public ExceptionHandleInfo handle(Throwable throwable) {
        LOGGER.info("httpRequestMethodNotSupportedExceptionHandler -> handle(Throwable throwable), throwable = {0}", throwable);
        //In the project, the resource operation is defined according to the request method + resource path,
        // and no separate request method verification is provided, because the authentication verification cannot be passed at all,
        // and only a 404 will be return.
        return new ExceptionHandleInfo(NOT_FOUND.status, new BlueResult<>(NOT_FOUND.code, null, NOT_FOUND.message));
    }
}

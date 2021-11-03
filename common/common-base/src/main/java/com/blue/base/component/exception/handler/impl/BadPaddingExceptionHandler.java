package com.blue.base.component.exception.handler.impl;

import com.blue.base.component.exception.handler.inter.ExceptionHandler;
import com.blue.base.component.exception.handler.model.ExceptionHandleInfo;
import com.blue.base.model.base.BlueResponse;
import reactor.util.Logger;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static reactor.util.Loggers.getLogger;

/**
 * RSA exp handler
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused"})
public final class BadPaddingExceptionHandler implements ExceptionHandler {

    private static final Logger LOGGER = getLogger(BadPaddingExceptionHandler.class);

    private static final String EXP_NAME = "javax.crypto.BadPaddingException";

    private static final ExceptionHandleInfo EXP_HANDLE_INFO = new ExceptionHandleInfo(BAD_REQUEST.status, new BlueResponse<>(BAD_REQUEST.code, null, "encrypt or decrypt failed"));

    @Override
    public String exceptionName() {
        return EXP_NAME;
    }

    @Override
    public ExceptionHandleInfo handle(Throwable throwable) {
        LOGGER.info("ExceptionHandleInfo handle(Throwable throwable), throwable = {0}", throwable);
        return EXP_HANDLE_INFO;
    }

}

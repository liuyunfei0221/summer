package com.blue.base.component.exception.handler.impl;

import com.blue.base.component.exception.handler.inter.ExceptionHandler;
import com.blue.base.component.exception.handler.model.ExceptionHandleInfo;
import com.blue.base.model.base.BlueResult;
import reactor.util.Logger;

import static com.blue.base.constant.base.ResponseElement.PAYLOAD_TOO_LARGE;
import static reactor.util.Loggers.getLogger;

/**
 * frame exp handler for netty
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused"})
public final class TooLongFrameExceptionHandler implements ExceptionHandler {

    private static final Logger LOGGER = getLogger(TooLongFrameExceptionHandler.class);

    private static final String EXP_NAME = "io.netty.handler.codec.TooLongFrameException";

    @Override
    public String exceptionName() {
        return EXP_NAME;
    }

    @Override
    public ExceptionHandleInfo handle(Throwable throwable) {
        LOGGER.info("tooLongFrameExceptionHandler -> handle(Throwable throwable), throwable = {0}", throwable);
        return new ExceptionHandleInfo(PAYLOAD_TOO_LARGE.status, new BlueResult<>(PAYLOAD_TOO_LARGE.code, null, PAYLOAD_TOO_LARGE.message));
    }

}

package com.blue.base.component.exception.handler.impl;

import com.blue.base.component.exception.handler.inter.ExceptionHandler;
import com.blue.base.component.exception.handler.model.ExceptionHandleInfo;
import com.blue.base.model.exps.BlueException;
import reactor.util.Logger;

import static reactor.util.Loggers.getLogger;

/**
 * blue exp handler
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused"})
public final class BlueExceptionHandler implements ExceptionHandler {

    private static final Logger LOGGER = getLogger(BlueExceptionHandler.class);

    private static final String EXP_NAME = "com.blue.base.model.exps.BlueException";

    @Override
    public String exceptionName() {
        return EXP_NAME;
    }

    @Override
    public ExceptionHandleInfo handle(Throwable throwable) {
        LOGGER.info("blueExceptionHandler -> handle(Throwable throwable), throwable = {0}", throwable);
        BlueException ex = (BlueException) throwable;

        return new ExceptionHandleInfo(ex.getStatus(), ex.getCode(), ex.getFillings());
    }
}

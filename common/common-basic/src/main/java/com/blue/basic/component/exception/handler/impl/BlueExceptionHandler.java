package com.blue.basic.component.exception.handler.impl;

import com.blue.basic.component.exception.handler.inter.ExceptionHandler;
import com.blue.basic.component.exception.model.ExceptionInfo;
import com.blue.basic.model.exps.BlueException;
import reactor.util.Logger;

import static reactor.util.Loggers.getLogger;

/**
 * blue exp handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused"})
public final class BlueExceptionHandler implements ExceptionHandler {

    private static final Logger LOGGER = getLogger(BlueExceptionHandler.class);

    private static final String EXP_NAME = "com.blue.basic.model.exps.BlueException";

    @Override
    public String exceptionName() {
        return EXP_NAME;
    }

    @Override
    public ExceptionInfo handle(Throwable throwable) {
        LOGGER.info("blueExceptionHandler -> handle(Throwable throwable), throwable = {0}", throwable);
        BlueException ex = (BlueException) throwable;

        return new ExceptionInfo(ex);
    }
}

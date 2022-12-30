package com.blue.basic.component.exception.handler.impl;

import com.blue.basic.component.exception.handler.inter.ExceptionHandler;
import com.blue.basic.component.exception.model.common.ExceptionInfo;
import org.springframework.web.server.ResponseStatusException;
import reactor.util.Logger;

import static com.blue.basic.common.base.ConstantProcessor.getResponseElementByStatus;
import static reactor.util.Loggers.getLogger;

/**
 * resp status exp handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused"})
public final class ResponseStatusExceptionHandler implements ExceptionHandler {

    private static final Logger LOGGER = getLogger(ResponseStatusExceptionHandler.class);

    private static final String EXP_NAME = "org.springframework.web.server.ResponseStatusException";

    @Override
    public String exceptionName() {
        return EXP_NAME;
    }

    @Override
    public ExceptionInfo handle(Throwable throwable) {
        LOGGER.info("responseStatusExceptionHandler -> handle(Throwable throwable), throwable = {0}", throwable);
        return new ExceptionInfo(getResponseElementByStatus(((ResponseStatusException) throwable).getStatus().value()));
    }
}

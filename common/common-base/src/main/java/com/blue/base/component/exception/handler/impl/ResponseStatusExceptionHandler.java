package com.blue.base.component.exception.handler.impl;

import com.blue.base.component.exception.handler.inter.ExceptionHandler;
import com.blue.base.component.exception.handler.model.ExceptionInfo;
import com.blue.base.constant.base.ResponseElement;
import org.springframework.web.server.ResponseStatusException;
import reactor.util.Logger;

import static com.blue.base.common.base.ConstantProcessor.getResponseElementByStatus;
import static reactor.util.Loggers.getLogger;

/**
 * resp status exp handler
 *
 * @author DarkBlue
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
        ResponseElement responseElement = getResponseElementByStatus(((ResponseStatusException) throwable).getStatus().value());
        return new ExceptionInfo(responseElement.status, responseElement.code, null);
    }
}

package com.blue.base.component.exception.handler.impl;

import com.blue.base.component.exception.handler.inter.ExceptionHandler;
import com.blue.base.component.exception.handler.model.ExceptionHandleInfo;
import com.blue.base.constant.base.ResponseElement;
import com.blue.base.model.base.BlueResult;
import org.springframework.web.server.ResponseStatusException;
import reactor.util.Logger;

import static com.blue.base.common.base.ConstantProcessor.getResponseElementByStatus;
import static reactor.util.Loggers.getLogger;

/**
 * http状态异常处理类
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
    public ExceptionHandleInfo handle(Throwable throwable) {
        LOGGER.info("responseStatusExceptionHandler -> handle(Throwable throwable), throwable = {0}", throwable);

        ResponseStatusException ex = (ResponseStatusException) throwable;
        ResponseElement responseElement = getResponseElementByStatus(ex.getStatus().value());

        return new ExceptionHandleInfo(responseElement.status, new BlueResult<>(responseElement.code, null, responseElement.message));
    }
}

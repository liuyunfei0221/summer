package com.blue.base.component.exception.handler.impl;

import com.blue.base.component.exception.handler.inter.ExceptionHandler;
import com.blue.base.component.exception.handler.model.ExceptionHandleInfo;
import com.blue.base.model.base.BlueResult;
import reactor.util.Logger;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static reactor.util.Loggers.getLogger;

/**
 * input exp handler
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused"})
public final class ServerWebInputExceptionHandler implements ExceptionHandler {

    private static final Logger LOGGER = getLogger(ServerWebInputExceptionHandler.class);

    private static final String EXP_NAME = "org.springframework.web.server.ServerWebInputException";

    @Override
    public String exceptionName() {
        return EXP_NAME;
    }

    @Override
    public ExceptionHandleInfo handle(Throwable throwable) {
        LOGGER.info("serverWebInputExceptionHandler -> handle(Throwable throwable), throwable = {0}", throwable);
        return new ExceptionHandleInfo(BAD_REQUEST.status, new BlueResult<>(BAD_REQUEST.code, null, "json数据解析失败"));
    }

}

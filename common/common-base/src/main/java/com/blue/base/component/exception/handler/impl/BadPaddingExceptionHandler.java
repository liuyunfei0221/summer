package com.blue.base.component.exception.handler.impl;

import com.blue.base.component.exception.handler.inter.ExceptionHandler;
import com.blue.base.component.exception.handler.model.ExceptionHandleInfo;
import com.blue.base.model.base.BlueResult;
import reactor.util.Logger;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static reactor.util.Loggers.getLogger;

/**
 * RSA解密异常处理类
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused"})
public final class BadPaddingExceptionHandler implements ExceptionHandler {

    private static final Logger LOGGER = getLogger(BadPaddingExceptionHandler.class);

    private static final String EXP_NAME = "javax.crypto.BadPaddingException";

    @Override
    public String exceptionName() {
        return EXP_NAME;
    }

    @Override
    public ExceptionHandleInfo handle(Throwable throwable) {
        LOGGER.info("badPaddingExceptionHandler -> handle(Throwable throwable), throwable = {0}", throwable);
        return new ExceptionHandleInfo(BAD_REQUEST.status, new BlueResult<>(BAD_REQUEST.code, null, "数据加解密失败"));
    }

}

package com.blue.base.component.exception.handler.impl;

import com.blue.base.component.exception.handler.inter.ExceptionHandler;
import com.blue.base.component.exception.handler.model.ExceptionHandleInfo;
import com.blue.base.model.base.BlueResult;
import reactor.util.Logger;

import static com.blue.base.constant.base.ResponseElement.NOT_FOUND;
import static reactor.util.Loggers.getLogger;

/**
 * 不支持的请求方式异常处理类
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
        //项目中是按请求方式+资源路径来定义资源操作,不提供单独的请求方式校验,因为根本无法通过鉴权校验,只会返回404
        return new ExceptionHandleInfo(NOT_FOUND.status, new BlueResult<>(NOT_FOUND.code, null, NOT_FOUND.message));
    }
}

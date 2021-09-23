package com.blue.base.component.exception.handler.impl;

import com.blue.base.component.exception.handler.inter.ExceptionHandler;
import com.blue.base.component.exception.handler.model.ExceptionHandleInfo;
import com.blue.base.model.base.BlueResult;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;
import reactor.util.Logger;

import static com.blue.base.constant.base.ResponseElement.UNSUPPORTED_MEDIA_TYPE;
import static com.blue.base.constant.base.Symbol.PATH_SEPARATOR;
import static com.blue.base.constant.base.Symbol.UNKNOWN;
import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;

/**
 * http请求类型异常处理类
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused"})
public final class UnsupportedMediaTypeStatusExceptionHandler implements ExceptionHandler {

    private static final Logger LOGGER = getLogger(UnsupportedMediaTypeStatusExceptionHandler.class);

    private static final String EXP_NAME = "org.springframework.web.server.UnsupportedMediaTypeStatusException";

    @Override
    public String exceptionName() {
        return EXP_NAME;
    }

    @Override
    public ExceptionHandleInfo handle(Throwable throwable) {
        LOGGER.info("unsupportedMediaTypeStatusExceptionHandler -> handle(Throwable throwable), throwable = {0}", throwable);

        UnsupportedMediaTypeStatusException ex = (UnsupportedMediaTypeStatusException) throwable;
        return new ExceptionHandleInfo(UNSUPPORTED_MEDIA_TYPE.status,
                new BlueResult<>(UNSUPPORTED_MEDIA_TYPE.code, null,
                        "不支持的媒体类型 -> " + ofNullable(ex.getContentType()).map(c -> c.getType() + PATH_SEPARATOR + c.getSubtype()).orElse(UNKNOWN.identity)));
    }
}

package com.blue.basic.component.exception.handler.impl;

import com.blue.basic.component.exception.handler.inter.ExceptionHandler;
import com.blue.basic.component.exception.model.common.ExceptionInfo;
import org.slf4j.Logger;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;

import static com.blue.basic.constant.common.ResponseElement.UNSUPPORTED_MEDIA_TYPE;
import static com.blue.basic.constant.common.Symbol.SLASH;
import static com.blue.basic.constant.common.Symbol.UNKNOWN;
import static java.util.Optional.ofNullable;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * un support exp handler
 *
 * @author liuyunfei
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
    public ExceptionInfo handle(Throwable throwable) {
        LOGGER.info("unsupportedMediaTypeStatusExceptionHandler -> handle(Throwable throwable), throwable = {0}", throwable);
        return new ExceptionInfo(UNSUPPORTED_MEDIA_TYPE, new String[]{"not support media type -> " +
                ofNullable(((UnsupportedMediaTypeStatusException) throwable).getContentType())
                        .map(c -> c.getType() + SLASH + c.getSubtype())
                        .orElse(UNKNOWN.identity)});
    }

}
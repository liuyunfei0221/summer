package com.blue.base.component.exception.handler.impl;

import com.blue.base.component.exception.handler.inter.ExceptionHandler;
import com.blue.base.component.exception.handler.model.ExceptionInfo;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;
import reactor.util.Logger;

import static com.blue.base.constant.base.ResponseElement.UNSUPPORTED_MEDIA_TYPE;
import static com.blue.base.constant.base.Symbol.PATH_SEPARATOR;
import static com.blue.base.constant.base.Symbol.UNKNOWN;
import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;

/**
 * un support exp handler
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
    public ExceptionInfo handle(Throwable throwable) {
        LOGGER.info("unsupportedMediaTypeStatusExceptionHandler -> handle(Throwable throwable), throwable = {0}", throwable);
        return new ExceptionInfo(UNSUPPORTED_MEDIA_TYPE, new String[]{"not support media type -> " +
                ofNullable(((UnsupportedMediaTypeStatusException) throwable).getContentType())
                        .map(c -> c.getType() + PATH_SEPARATOR + c.getSubtype())
                        .orElse(UNKNOWN.identity)});
    }
}

package com.blue.base.component.exception.handler.impl;

import com.blue.base.component.exception.handler.inter.ExceptionHandler;
import com.blue.base.component.exception.handler.model.ExceptionHandleInfo;
import com.blue.base.model.base.BlueResponse;
import org.springframework.util.StringUtils;
import reactor.util.Logger;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;

/**
 * ill arg exp handler
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused"})
public final class IllegalArgumentExceptionHandler implements ExceptionHandler {

    private static final Logger LOGGER = getLogger(IllegalArgumentExceptionHandler.class);

    private static final String EXP_NAME = "java.lang.IllegalArgumentException";

    @Override
    public String exceptionName() {
        return EXP_NAME;
    }

    @Override
    public ExceptionHandleInfo handle(Throwable throwable) {
        LOGGER.info("illegalArgumentExceptionHandler -> handle(Throwable throwable), throwable = {0}", throwable);
        return new ExceptionHandleInfo(BAD_REQUEST.status, new BlueResponse<>(BAD_REQUEST.code, null,
                ofNullable(throwable.getMessage()).filter(StringUtils::hasText).orElse(BAD_REQUEST.message)));
    }

}

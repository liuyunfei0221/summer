package com.blue.basic.component.exception.handler.impl;

import com.blue.basic.component.exception.handler.inter.ExceptionHandler;
import com.blue.basic.component.exception.model.common.ExceptionInfo;
import org.slf4j.Logger;
import org.springframework.util.StringUtils;

import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;
import static java.util.Optional.ofNullable;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * ill arg exp handler
 *
 * @author liuyunfei
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
    public ExceptionInfo handle(Throwable throwable) {
        LOGGER.info("illegalArgumentExceptionHandler -> handle(Throwable throwable), throwable = {0}", throwable);
        return new ExceptionInfo(BAD_REQUEST, new String[]{ofNullable(throwable.getMessage()).filter(StringUtils::hasText).orElse(BAD_REQUEST.message)});
    }

}
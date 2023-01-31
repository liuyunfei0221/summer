package com.blue.basic.component.exception.handler.impl;

import com.blue.basic.component.exception.handler.inter.ExceptionHandler;
import com.blue.basic.component.exception.model.common.ExceptionInfo;
import reactor.util.Logger;

import java.util.function.Function;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.constant.common.ResponseElement.DATA_ALREADY_EXIST;
import static com.blue.basic.constant.common.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_VALUE;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.contains;
import static reactor.util.Loggers.getLogger;

/**
 * mongo write exp handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused"})
public class MongoWriteExceptionHandler implements ExceptionHandler {

    private static final Logger LOGGER = getLogger(MongoWriteExceptionHandler.class);

    private static final String EXP_NAME = "com.mongodb.MongoWriteException";

    private static final ExceptionInfo DUPLICATE_DATA_HANDLE_INFO = new ExceptionInfo(DATA_ALREADY_EXIST);
    private static final ExceptionInfo EXP_HANDLE_INFO = new ExceptionInfo(INTERNAL_SERVER_ERROR);

    private static final String DUPLICATE_MESSAGE_PRE = "duplicate";

    private static final Function<RuntimeException, ExceptionInfo> INFO_CONVERTER = exp -> {
        if (isNull(exp))
            return EXP_HANDLE_INFO;

        return contains(ofNullable(exp.getMessage()).orElse(EMPTY_VALUE.value), DUPLICATE_MESSAGE_PRE) ? DUPLICATE_DATA_HANDLE_INFO : EXP_HANDLE_INFO;
    };


    @Override
    public String exceptionName() {
        return EXP_NAME;
    }

    @Override
    public ExceptionInfo handle(Throwable throwable) {
        LOGGER.info("mongoWriteExceptionHandler -> handle(Throwable throwable), throwable = {0}", throwable);

        RuntimeException exception = (RuntimeException) throwable;
        return INFO_CONVERTER.apply(exception);
    }

}
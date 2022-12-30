package com.blue.basic.component.exception.handler.impl;

import com.blue.basic.component.exception.handler.inter.ExceptionHandler;
import com.blue.basic.component.exception.model.common.ExceptionInfo;
import reactor.util.Logger;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.function.Function;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.constant.common.ResponseElement.DATA_ALREADY_EXIST;
import static com.blue.basic.constant.common.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_VALUE;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.startsWith;
import static reactor.util.Loggers.getLogger;

/**
 * sql exp handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused"})
public class SQLIntegrityConstraintViolationExceptionHandler implements ExceptionHandler {

    private static final Logger LOGGER = getLogger(SQLIntegrityConstraintViolationExceptionHandler.class);

    private static final String EXP_NAME = "java.sql.SQLIntegrityConstraintViolationException";

    private static final ExceptionInfo DUPLICATE_DATA_HANDLE_INFO = new ExceptionInfo(DATA_ALREADY_EXIST);
    private static final ExceptionInfo EXP_HANDLE_INFO = new ExceptionInfo(INTERNAL_SERVER_ERROR);

    private static final String DUPLICATE_MESSAGE_PRE = "Duplicate";

    private static final Function<SQLIntegrityConstraintViolationException, ExceptionInfo> INFO_CONVERTER = exp -> {
        if (isNull(exp))
            return EXP_HANDLE_INFO;

        return startsWith(ofNullable(exp.getMessage()).orElse(EMPTY_VALUE.value), DUPLICATE_MESSAGE_PRE) ? DUPLICATE_DATA_HANDLE_INFO : EXP_HANDLE_INFO;
    };


    @Override
    public String exceptionName() {
        return EXP_NAME;
    }

    @Override
    public ExceptionInfo handle(Throwable throwable) {
        LOGGER.info("SQLIntegrityConstraintViolationExceptionHandler -> handle(Throwable throwable), throwable = {0}", throwable);

        SQLIntegrityConstraintViolationException exception = (SQLIntegrityConstraintViolationException) throwable;
        return INFO_CONVERTER.apply(exception);
    }

}

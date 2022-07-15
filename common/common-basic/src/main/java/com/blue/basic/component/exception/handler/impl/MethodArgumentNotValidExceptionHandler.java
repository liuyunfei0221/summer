package com.blue.basic.component.exception.handler.impl;

import com.blue.basic.component.exception.handler.inter.ExceptionHandler;
import com.blue.basic.component.exception.model.ExceptionInfo;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import reactor.util.Logger;

import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;
import static java.util.Optional.of;
import static org.springframework.util.CollectionUtils.isEmpty;
import static reactor.util.Loggers.getLogger;

/**
 * invalid arg exp handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused"})
public final class MethodArgumentNotValidExceptionHandler implements ExceptionHandler {

    private static final Logger LOGGER = getLogger(MethodArgumentNotValidExceptionHandler.class);

    private static final String EXP_NAME = "org.springframework.web.bind.MethodArgumentNotValidException";

    @Override
    public String exceptionName() {
        return EXP_NAME;
    }

    @Override
    public ExceptionInfo handle(Throwable throwable) {
        LOGGER.info("methodArgumentNotValidExceptionHandler -> handle(Throwable throwable), throwable = {0}", throwable);
        MethodArgumentNotValidException ex = (MethodArgumentNotValidException) throwable;
        return new ExceptionInfo(BAD_REQUEST, new String[]{of(ex.getBindingResult())
                .map(BindingResult::getAllErrors)
                .filter(l -> !isEmpty(l))
                .map(l -> l.get(0))
                .map(ObjectError::getDefaultMessage)
                .orElse(BAD_REQUEST.message)});
    }
}

package com.blue.base.component.exception.handler.impl;

import com.blue.base.component.exception.handler.inter.ExceptionHandler;
import com.blue.base.component.exception.handler.model.ExceptionHandleInfo;
import com.blue.base.model.base.BlueResponse;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import reactor.util.Logger;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static java.util.Optional.of;
import static org.springframework.util.CollectionUtils.isEmpty;
import static reactor.util.Loggers.getLogger;

/**
 * invalid arg exp handler
 *
 * @author DarkBlue
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
    public ExceptionHandleInfo handle(Throwable throwable) {
        LOGGER.info("methodArgumentNotValidExceptionHandler -> handle(Throwable throwable), throwable = {0}", throwable);
        MethodArgumentNotValidException ex = (MethodArgumentNotValidException) throwable;
        return new ExceptionHandleInfo(BAD_REQUEST.status, new BlueResponse<>(BAD_REQUEST.code, null, of(ex.getBindingResult())
                .map(BindingResult::getAllErrors)
                .filter(l -> !isEmpty(l))
                .map(l -> l.get(0))
                .map(ObjectError::getDefaultMessage)
                .orElse(BAD_REQUEST.message)));
    }
}

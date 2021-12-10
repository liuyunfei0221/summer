package com.blue.base.component.exception.handler.impl;

import com.blue.base.component.exception.handler.inter.ExceptionHandler;
import com.blue.base.component.exception.handler.model.ExceptionInfo;
import reactor.util.Logger;

import static com.blue.base.constant.base.ResponseElement.UNAUTHORIZED;
import static reactor.util.Loggers.getLogger;

/**
 * auth exp handler
 *
 * @author liuyunfei
 * @date 2021/8/17
 * @apiNote
 */
@SuppressWarnings({"unused"})
public class AuthenticationExceptionHandler implements ExceptionHandler {

    private static final Logger LOGGER = getLogger(AuthenticationExceptionHandler.class);

    private static final String EXP_NAME = "com.blue.jwt.exception.AuthenticationException";

    private static final ExceptionInfo EXP_HANDLE_INFO = new ExceptionInfo(UNAUTHORIZED.status, UNAUTHORIZED.code, null);

    @Override
    public String exceptionName() {
        return EXP_NAME;
    }

    @Override
    public ExceptionInfo handle(Throwable throwable) {
        LOGGER.info("authenticationExceptionHandler -> handle(Throwable throwable), throwable = {0}", throwable);
        return EXP_HANDLE_INFO;
    }

}

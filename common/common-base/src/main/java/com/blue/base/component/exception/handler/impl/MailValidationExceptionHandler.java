package com.blue.base.component.exception.handler.impl;

import com.blue.base.component.exception.handler.inter.ExceptionHandler;
import com.blue.base.component.exception.handler.model.ExceptionInfo;
import reactor.util.Logger;

import static com.blue.base.constant.base.ResponseElement.INVALID_EMAIL_ADDRESS;
import static reactor.util.Loggers.getLogger;

/**
 * mail valid exp handler
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "SpellCheckingInspection"})
public class MailValidationExceptionHandler implements ExceptionHandler {

    private static final Logger LOGGER = getLogger(MailValidationExceptionHandler.class);

    private static final String EXP_NAME = "org.simplejavamail.mailer.MailValidationException";

    @Override
    public String exceptionName() {
        return EXP_NAME;
    }

    @Override
    public ExceptionInfo handle(Throwable throwable) {
        LOGGER.info("mailValidationExceptionHandler -> handle(Throwable throwable), throwable = {0}", throwable);
        RuntimeException ex = (RuntimeException) throwable;

        return new ExceptionInfo(INVALID_EMAIL_ADDRESS, new String[]{ex.getMessage()});
    }

}
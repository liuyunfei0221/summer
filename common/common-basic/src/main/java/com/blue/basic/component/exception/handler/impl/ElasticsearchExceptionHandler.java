package com.blue.basic.component.exception.handler.impl;

import com.blue.basic.component.exception.handler.inter.ExceptionHandler;
import com.blue.basic.component.exception.model.ExceptionInfo;
import reactor.util.Logger;

import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;
import static reactor.util.Loggers.getLogger;

/**
 * elastic search exp handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused"})
public final class ElasticsearchExceptionHandler implements ExceptionHandler {

    private static final Logger LOGGER = getLogger(ElasticsearchExceptionHandler.class);

    private static final String EXP_NAME = "co.elastic.clients.elasticsearch._types.ElasticsearchException";

    private static final ExceptionInfo EXP_HANDLE_INFO = new ExceptionInfo(BAD_REQUEST);

    @Override
    public String exceptionName() {
        return EXP_NAME;
    }

    @Override
    public ExceptionInfo handle(Throwable throwable) {
        LOGGER.info("elasticsearchExceptionHandler -> handle(Throwable throwable), throwable = {0}", throwable);
        return EXP_HANDLE_INFO;
    }
}

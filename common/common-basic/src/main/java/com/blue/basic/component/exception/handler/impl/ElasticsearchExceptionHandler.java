package com.blue.basic.component.exception.handler.impl;

import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.component.exception.handler.inter.ExceptionHandler;
import com.blue.basic.component.exception.model.common.ExceptionInfo;
import com.blue.basic.component.exception.model.es.Response;
import reactor.util.Logger;

import java.util.function.Function;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.common.base.CommonFunctions.GSON;
import static com.blue.basic.constant.common.ResponseElement.INVALID_REQUEST_PARAM;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_VALUE;
import static java.util.Optional.ofNullable;
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

    private static final Function<Throwable, String> MESSAGE_PARSER = throwable -> {
        if (isNull(throwable))
            return EMPTY_VALUE.value;

        try {
            return ofNullable(throwable.getMessage())
                    .filter(BlueChecker::isNotBlank)
                    .map(msg -> msg.split("\\n"))
                    .map(strArr -> strArr[1])
                    .map(resp -> GSON.fromJson(resp, Response.class))
                    .map(resp -> {
                        LOGGER.warn("es error resp = {}", resp);

                        return ofNullable(resp.getError())
                                .map(err ->
                                        "type-> " + ofNullable(err.getType()).orElse(EMPTY_VALUE.value)
                                                + ", reason-> " + ofNullable(err.getReason()).orElse(EMPTY_VALUE.value))
                                .orElse("UNKNOWN_ERROR");
                    })
                    .orElse("UNKNOWN_ERROR");
        } catch (Exception e) {
            return EMPTY_VALUE.value;
        }
    };

    @Override
    public String exceptionName() {
        return EXP_NAME;
    }

    @Override
    public ExceptionInfo handle(Throwable throwable) {
        LOGGER.info("elasticsearchExceptionHandler -> handle(Throwable throwable), throwable = {0}", throwable);
        return new ExceptionInfo(INVALID_REQUEST_PARAM, new String[]{MESSAGE_PARSER.apply(throwable)});
    }

}

package com.blue.base.component.exception.ioc;

import com.blue.base.component.exception.handler.ExceptionProcessor;
import com.blue.base.model.base.ExceptionResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;

import static com.blue.base.common.base.CommonFunctions.GSON;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.getAcceptLanguages;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static reactor.core.publisher.Mono.just;

/**
 * global exp handler bean
 *
 * @author DarkBlue
 */
@Component
@Order(HIGHEST_PRECEDENCE)
public final class ReactiveBlueExceptionHandler implements WebExceptionHandler {

    private static final Charset CHARSET = UTF_8;

    @SuppressWarnings("NullableProblems")
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable throwable) {
        ExceptionResponse exceptionResponse = ExceptionProcessor.handle(throwable, getAcceptLanguages(exchange.getRequest()));

        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().add(CONTENT_TYPE, APPLICATION_JSON_VALUE);
        response.setRawStatusCode(exceptionResponse.getStatus());

        return response.writeWith(just(response.bufferFactory().wrap(GSON.toJson(exceptionResponse).getBytes(CHARSET))));
    }

}

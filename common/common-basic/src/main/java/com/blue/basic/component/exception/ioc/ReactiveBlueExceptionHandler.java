package com.blue.basic.component.exception.ioc;

import com.blue.basic.component.exception.handler.ExceptionProcessor;
import com.blue.basic.model.common.ExceptionElement;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;

import static com.blue.basic.common.base.CommonFunctions.GSON;
import static com.blue.basic.common.base.CommonFunctions.EXP_ELE_2_RESP;
import static com.blue.basic.common.base.CommonFunctions.getAcceptLanguages;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static reactor.core.publisher.Mono.just;

/**
 * global exp handler bean
 *
 * @author liuyunfei
 */
@Component
@Order(HIGHEST_PRECEDENCE)
public final class ReactiveBlueExceptionHandler implements WebExceptionHandler {

    private static final Charset CHARSET = UTF_8;

    @SuppressWarnings("NullableProblems")
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable throwable) {
        ExceptionElement exceptionElement = ExceptionProcessor.handle(throwable, getAcceptLanguages(exchange.getRequest()));

        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().add(CONTENT_TYPE, APPLICATION_JSON_VALUE);
        response.setRawStatusCode(exceptionElement.getStatus());

        return response.writeWith(just(response.bufferFactory()
                .wrap(GSON.toJson(EXP_ELE_2_RESP.apply(exceptionElement)).getBytes(CHARSET))));
    }

}

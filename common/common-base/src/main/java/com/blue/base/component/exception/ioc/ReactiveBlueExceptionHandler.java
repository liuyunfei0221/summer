package com.blue.base.component.exception.ioc;

import com.blue.base.component.exception.common.ExceptionProcessor;
import com.blue.base.component.exception.handler.model.ExceptionHandleInfo;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import static com.blue.base.common.base.CommonFunctions.GSON;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.getAcceptLanguages;
import static com.blue.base.component.exception.common.MessagesReader.loadMessages;
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
    private static final String MESSAGES_URI = "classpath:i18n";

    private static final Map<String, Map<String, String>> I_18_N = loadMessages(MESSAGES_URI);

    @SuppressWarnings("NullableProblems")
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable throwable) {
        List<String> acceptLanguages = getAcceptLanguages(exchange.getRequest());
        System.err.println(acceptLanguages);
        System.err.println(I_18_N);

        ExceptionHandleInfo exceptionHandleInfo = ExceptionProcessor.handle(throwable);

        ServerHttpResponse response = exchange.getResponse();

        response.getHeaders().add(CONTENT_TYPE, APPLICATION_JSON_VALUE);
        response.setRawStatusCode(exceptionHandleInfo.getStatus());

        return response.writeWith(just(response.bufferFactory().wrap(GSON.toJson(exceptionHandleInfo.getBlueVo()).getBytes(CHARSET))));
    }

}

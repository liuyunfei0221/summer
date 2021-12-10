package com.blue.base.component.exception.ioc;

import com.blue.base.component.exception.common.ExceptionProcessor;
import com.blue.base.component.exception.handler.model.ExceptionHandleInfo;
import com.blue.base.model.base.ExceptionResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static com.blue.base.common.base.CommonFunctions.GSON;
import static com.blue.base.common.base.FileProcessor.getFiles;
import static com.blue.base.common.base.PropertiesProcessor.loadProp;
import static com.blue.base.common.base.PropertiesProcessor.parseProp;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.getAcceptLanguages;
import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.base.constant.base.Symbol.SCHEME_SEPARATOR;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.StringUtils.lastIndexOf;
import static org.apache.commons.lang3.StringUtils.substring;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static reactor.core.publisher.Mono.just;

/**
 * global exp handler bean
 *
 * @author DarkBlue
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
@Component
@Order(HIGHEST_PRECEDENCE)
public final class ReactiveBlueExceptionHandler implements WebExceptionHandler {

    private static final Charset CHARSET = UTF_8;
    private static final String MESSAGES_URI = "classpath:i18n";
    private static final String DEFAULT_MESSAGES = "en_us";
    private static final String DEFAULT_MESSAGE = INTERNAL_SERVER_ERROR.message;
    private static final ExceptionResponse DEFAULT_EXP_RESP = new ExceptionResponse;

    private static final BiFunction<Integer, String, ExceptionResponse> EXP_RESP_GEN = (code, msg) ->
            new ExceptionResponse(code, msg);

    private static final UnaryOperator<String> PRE_NAME_PARSER = n -> {
        int idx = lastIndexOf(n, SCHEME_SEPARATOR.identity);
        return idx >= 0 ? (idx > 0 ? substring(n, 0, idx) : "") : n;
    };

    public static final Function<String, Map<String, Map<String, String>>> MESSAGES_LOADER = uri ->
            getFiles(uri, true).stream()
                    .collect(toMap(f -> requireNonNull(PRE_NAME_PARSER).apply(f.getName()).toLowerCase(), f -> parseProp(loadProp(f)), (a, b) -> a));

    private static final Map<String, Map<String, String>> I_18_N = MESSAGES_LOADER.apply(MESSAGES_URI);

    private static final Function<List<String>, Map<String, String>> MESSAGES_GETTER = languages -> {
        Map<String, String> messages;
        for (String language : languages)
            if ((messages = I_18_N.get(language)) != null)
                return messages;

        return I_18_N.get(DEFAULT_MESSAGES);
    };

    private static final BiFunction<List<String>, String, String> MESSAGE_GETTER = (languages, key) ->
            ofNullable(MESSAGES_GETTER.apply(languages))
                    .map(messages -> messages.get(key))
                    .orElse(DEFAULT_MESSAGE);


    private static final BiFunction<List<String>, ExceptionHandleInfo, ExceptionResponse> EXP_RES_GETTER = (languages, info) ->
            ofNullable(info)
                    .map(ExceptionHandleInfo::getCode)
                    .map(code -> MESSAGE_GETTER.apply(languages, String.valueOf(code)))
                    .map(msg -> String.format(msg, (Object[]) info.getFillings()))
                    .map(msg -> EXP_RESP_GEN.apply(info.getCode(), msg))
                    .orElse(DEFAULT_EXP_RESP);


    @SuppressWarnings("NullableProblems")
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable throwable) {
        ExceptionHandleInfo exceptionHandleInfo = ExceptionProcessor.handle(throwable);

        ExceptionResponse exceptionResponse = EXP_RES_GETTER.apply(getAcceptLanguages(exchange.getRequest()), exceptionHandleInfo);

        ServerHttpResponse response = exchange.getResponse();

        response.getHeaders().add(CONTENT_TYPE, APPLICATION_JSON_VALUE);
        response.setRawStatusCode(exceptionHandleInfo.getStatus());

        return response.writeWith(just(response.bufferFactory().wrap(GSON.toJson(exceptionResponse).getBytes(CHARSET))));
    }

}

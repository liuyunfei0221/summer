package com.blue.captcha.config.filter.global;

import com.blue.base.component.exception.handler.model.ExceptionHandleInfo;
import com.blue.base.constant.base.BlueHeader;
import com.blue.base.model.base.DataEvent;
import com.blue.captcha.common.CaptchaCommonFactory;
import com.blue.captcha.component.RequestEventReporter;
import com.google.gson.Gson;
import org.springframework.core.Ordered;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.blue.base.common.reactive.ReactiveCommonFunctions.getIp;
import static com.blue.base.constant.base.BlueDataAttrKey.*;
import static com.blue.base.constant.base.DataEventType.UNIFIED;
import static com.blue.captcha.config.filter.BlueFilterOrder.BLUE_ERROR_REPORT;
import static java.lang.String.valueOf;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * error reporter
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "SpringJavaInjectionPointsAutowiringInspection"})
@Component
public final class BlueErrorReportFilter implements WebFilter, Ordered {

    private static final Logger LOGGER = getLogger(BlueErrorReportFilter.class);

    private final ExecutorService executorService;

    private final RequestEventReporter requestEventReporter;

    public BlueErrorReportFilter(ExecutorService executorService, RequestEventReporter requestEventReporter) {
        this.executorService = executorService;
        this.requestEventReporter = requestEventReporter;
    }

    private static final Consumer<String> SCHEMA_ASSERTER = CaptchaCommonFactory.SCHEMA_ASSERTER;
    private static final Consumer<String> METHOD_VALUE_ASSERTER = CaptchaCommonFactory.METHOD_VALUE_ASSERTER;

    private static final String AUTHORIZATION = BlueHeader.AUTHORIZATION.name;

    private static final List<HttpMessageReader<?>> MESSAGE_READERS = CaptchaCommonFactory.MESSAGE_READERS;

    private static final Supplier<Long> TIME_STAMP_GETTER = CaptchaCommonFactory.TIME_STAMP_GETTER;

    private static final Gson GSON = CaptchaCommonFactory.GSON;

    private static final Function<Throwable, ExceptionHandleInfo> THROWABLE_CONVERTER = CaptchaCommonFactory.THROWABLE_CONVERTER;

    private static final Supplier<String> RANDOM_KEY_GENERATOR = CaptchaCommonFactory.RANDOM_KEY_GETTER;

    private void report(DataEvent dataEvent, Throwable throwable) {
        try {
            executorService.submit(() -> {
                ExceptionHandleInfo exceptionHandleInfo = THROWABLE_CONVERTER.apply(throwable);

                dataEvent.addData(RESPONSE_STATUS.key, valueOf(exceptionHandleInfo.getCode()).intern());
                dataEvent.addData(RESPONSE_BODY.key, GSON.toJson(exceptionHandleInfo.getBlueVo()));

                requestEventReporter.report(dataEvent);
                LOGGER.info("report exception event, dataEvent = {}", dataEvent);
            });
        } catch (Exception e) {
            LOGGER.error("report failed, dataEvent = {}, throwable = {}, e = {}",
                    dataEvent, throwable, e);
        }
    }

    @SuppressWarnings({"NullableProblems", "DuplicatedCode"})
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        SCHEMA_ASSERTER.accept(request.getURI().getScheme());

        String methodValue = request.getMethodValue();
        METHOD_VALUE_ASSERTER.accept(methodValue);

        String requestId = RANDOM_KEY_GENERATOR.get();
        String clientIp = getIp(request);

        Map<String, Object> attributes = exchange.getAttributes();

        attributes.put(REQUEST_ID.key, requestId);
        attributes.put(CLIENT_IP.key, clientIp);

        ofNullable(request.getHeaders().getFirst(BlueHeader.METADATA.name))
                .ifPresent(metadata -> attributes.put(METADATA.key, metadata));
        ofNullable(request.getHeaders().getFirst(AUTHORIZATION))
                .ifPresent(jwt -> attributes.put(JWT.key, jwt));

        return chain.filter(exchange)
                .onErrorResume(throwable ->
                        ServerRequest.create(exchange, MESSAGE_READERS)
                                .bodyToMono(String.class)
                                .switchIfEmpty(
                                        just(""))
                                .flatMap(requestBody -> {
                                    DataEvent dataEvent = new DataEvent();

                                    dataEvent.setDataEventType(UNIFIED);
                                    dataEvent.setStamp(TIME_STAMP_GETTER.get());

                                    dataEvent.addData(REQUEST_ID.key, requestId);
                                    dataEvent.addData(CLIENT_IP.key, clientIp);

                                    ofNullable(attributes.get(METADATA.key)).map(String::valueOf)
                                            .ifPresent(metadata -> dataEvent.addData(METADATA.key, metadata));
                                    ofNullable(attributes.get(JWT.key)).map(String::valueOf)
                                            .ifPresent(jwt -> dataEvent.addData(JWT.key, jwt));
                                    ofNullable(attributes.get(ACCESS.key)).map(String::valueOf)
                                            .ifPresent(access -> dataEvent.addData(ACCESS.key, access));

                                    dataEvent.addData(METHOD.key, methodValue.intern());
                                    dataEvent.addData(URI.key, request.getURI().getRawPath().intern());

                                    if (!"".equals(requestBody))
                                        dataEvent.addData(REQUEST_BODY.key, requestBody);

                                    report(dataEvent, throwable);
                                    return error(throwable);
                                }));
    }

    @Override
    public int getOrder() {
        return BLUE_ERROR_REPORT.order;
    }

}

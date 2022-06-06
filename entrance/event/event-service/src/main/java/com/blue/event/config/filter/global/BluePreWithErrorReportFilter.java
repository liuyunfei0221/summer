package com.blue.event.config.filter.global;

import com.blue.base.constant.base.BlueHeader;
import com.blue.base.model.common.DataEvent;
import com.blue.base.model.common.ExceptionResponse;
import com.blue.event.component.event.RequestEventReporter;
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
import java.util.function.BiConsumer;

import static com.blue.base.common.base.CommonFunctions.*;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.getAcceptLanguages;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.getIp;
import static com.blue.base.constant.base.BlueDataAttrKey.*;
import static com.blue.base.constant.base.BlueHeader.REQUEST_IP;
import static com.blue.base.constant.base.DataEventOpType.CLICK;
import static com.blue.base.constant.base.DataEventType.UNIFIED;
import static com.blue.event.config.filter.BlueFilterOrder.BLUE_PRE_WITH_ERROR_REPORT;
import static java.lang.String.valueOf;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * error reporter
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "UnusedAssignment", "SpringJavaInjectionPointsAutowiringInspection"})
@Component
public final class BluePreWithErrorReportFilter implements WebFilter, Ordered {

    private static final Logger LOGGER = getLogger(BluePreWithErrorReportFilter.class);

    private final List<HttpMessageReader<?>> httpMessageReaders;

    private final ExecutorService executorService;

    private final RequestEventReporter requestEventReporter;

    public BluePreWithErrorReportFilter(List<HttpMessageReader<?>> httpMessageReaders, ExecutorService executorService, RequestEventReporter requestEventReporter) {
        this.httpMessageReaders = httpMessageReaders;
        this.executorService = executorService;
        this.requestEventReporter = requestEventReporter;
    }

    private void report(Throwable throwable, ServerHttpRequest request, DataEvent dataEvent) {
        try {
            executorService.execute(() -> {
                ExceptionResponse exceptionResponse = THROWABLE_CONVERTER.apply(throwable, getAcceptLanguages(request));

                dataEvent.addData(RESPONSE_STATUS.key, valueOf(exceptionResponse.getStatus()).intern());
                dataEvent.addData(RESPONSE_BODY.key, GSON.toJson(exceptionResponse));

                requestEventReporter.report(dataEvent);
                LOGGER.info("report exception event, dataEvent = {}", dataEvent);

                exceptionResponse = null;
            });
        } catch (Exception e) {
            LOGGER.error("report failed, dataEvent = {}, throwable = {}, e = {}",
                    dataEvent, throwable, e);
        }
    }

    private static final BiConsumer<ServerHttpRequest, String> REQUEST_IP_REPACKAGER =
            (request, ip) -> request.mutate().headers(hs -> hs.set(REQUEST_IP.name, ip));

    private void packageAttr(ServerHttpRequest request, Map<String, Object> attributes) {
        String method = request.getMethodValue().intern();
        METHOD_VALUE_ASSERTER.accept(method);
        SCHEMA_ASSERTER.accept(request.getURI().getScheme());

        String realUri = request.getPath().value();
        String ip = getIp(request);

        attributes.put(REQUEST_ID.key, request.getId());
        attributes.put(CLIENT_IP.key, ip);
        attributes.put(METHOD.key, method);
        attributes.put(REAL_URI.key, realUri);
        attributes.put(URI.key, REQUEST_REST_URI_PROCESSOR.apply(realUri).intern());

        ofNullable(request.getHeaders().getFirst(BlueHeader.METADATA.name))
                .ifPresent(metadata -> attributes.put(METADATA.key, metadata));
        ofNullable(request.getHeaders().getFirst(AUTHORIZATION))
                .ifPresent(jwt -> attributes.put(JWT.key, jwt));
        ofNullable(request.getHeaders().getFirst(BlueHeader.HOST.name))
                .ifPresent(host -> attributes.put(HOST.key, host));
        ofNullable(request.getHeaders().getFirst(BlueHeader.USER_AGENT.name))
                .ifPresent(userAgent -> attributes.put(USER_AGENT.key, userAgent));

        REQUEST_IP_REPACKAGER.accept(request, ip);
    }

    @SuppressWarnings({"NullableProblems", "DuplicatedCode"})
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        Map<String, Object> attributes = exchange.getAttributes();

        packageAttr(request, attributes);

        return chain.filter(exchange)
                .onErrorResume(throwable ->
                        ServerRequest.create(exchange, httpMessageReaders)
                                .bodyToMono(String.class)
                                .switchIfEmpty(defer(() -> just("")))
                                .flatMap(requestBody -> {
                                    DataEvent dataEvent = new DataEvent();
                                    dataEvent.setDataEventType(UNIFIED);
                                    dataEvent.setDataEventOpType(CLICK);

                                    dataEvent.setStamp(TIME_STAMP_GETTER.get());
                                    EVENT_PACKAGER.accept(attributes, dataEvent);
                                    if (!"".equals(requestBody))
                                        dataEvent.addData(REQUEST_BODY.key, requestBody);
                                    report(throwable, request, dataEvent);

                                    return error(throwable);
                                }));
    }

    @Override
    public int getOrder() {
        return BLUE_PRE_WITH_ERROR_REPORT.order;
    }

}

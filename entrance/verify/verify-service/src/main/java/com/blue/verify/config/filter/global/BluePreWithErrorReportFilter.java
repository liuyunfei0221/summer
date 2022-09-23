package com.blue.verify.config.filter.global;

import com.blue.basic.constant.common.BlueHeader;
import com.blue.basic.model.common.ExceptionElement;
import com.blue.basic.model.event.DataEvent;
import com.blue.verify.component.event.RequestEventReporter;
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

import static com.blue.basic.common.base.CommonFunctions.*;
import static com.blue.basic.constant.common.BlueDataAttrKey.*;
import static com.blue.basic.constant.common.BlueHeader.AUTHORIZATION;
import static com.blue.basic.constant.common.BlueHeader.REQUEST_IP;
import static com.blue.basic.constant.common.DataEventOpType.CLICK;
import static com.blue.basic.constant.common.DataEventType.UNIFIED;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_VALUE;
import static com.blue.verify.config.filter.BlueFilterOrder.BLUE_PRE_WITH_ERROR_REPORT;
import static java.lang.String.valueOf;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * error reporter
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "UnusedAssignment"})
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
                ExceptionElement exceptionElement = THROWABLE_CONVERTER.apply(throwable, getAcceptLanguages(request));

                dataEvent.addData(RESPONSE_STATUS.key, valueOf(exceptionElement.getStatus()).intern());
                dataEvent.addData(RESPONSE_BODY.key, GSON.toJson(EXP_ELE_2_RESP.apply(exceptionElement)));

                requestEventReporter.report(dataEvent);
                LOGGER.info("report exception event, dataEvent = {}", dataEvent);

                exceptionElement = null;
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

        ofNullable(request.getHeaders().getFirst(AUTHORIZATION.name))
                .ifPresent(jwt -> attributes.put(JWT.key, jwt));
        ofNullable(request.getHeaders().getFirst(BlueHeader.USER_AGENT.name))
                .ifPresent(userAgent -> attributes.put(USER_AGENT.key, userAgent));
        ofNullable(request.getHeaders().getFirst(BlueHeader.METADATA.name))
                .ifPresent(metadata -> attributes.put(METADATA.key, metadata));
        ofNullable(request.getHeaders().getFirst(BlueHeader.SOURCE.name))
                .ifPresent(source -> attributes.put(SOURCE.key, source));
        ofNullable(request.getHeaders().getFirst(BlueHeader.HOST.name))
                .ifPresent(host -> attributes.put(HOST.key, host));
        ofNullable(request.getHeaders().getFirst(BlueHeader.REQUEST_EXTRA.name))
                .ifPresent(extra -> attributes.put(REQUEST_EXTRA.key, extra));

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
                                .switchIfEmpty(defer(() -> just(EMPTY_VALUE.value)))
                                .flatMap(requestBody -> {
                                    DataEvent dataEvent = new DataEvent();
                                    dataEvent.setDataEventOpType(CLICK.identity);

                                    dataEvent.setDataEventType(UNIFIED.identity);
                                    dataEvent.setStamp(TIME_STAMP_GETTER.get());
                                    EVENT_ATTR_PACKAGER.accept(attributes, dataEvent);
                                    if (!EMPTY_VALUE.value.equals(requestBody))
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

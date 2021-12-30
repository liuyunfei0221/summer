package com.blue.verify.config.filter.global;

import com.blue.base.common.content.common.RequestBodyProcessor;
import com.blue.base.model.base.DataEvent;
import com.blue.base.model.base.ExceptionResponse;
import com.blue.verify.component.RequestEventReporter;
import com.blue.verify.config.deploy.ResponseDeploy;
import org.reactivestreams.Publisher;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static com.blue.base.common.base.CommonFunctions.*;
import static com.blue.base.constant.base.BlueDataAttrKey.*;
import static com.blue.base.constant.base.DataEventType.UNIFIED;
import static com.blue.verify.common.VerifyCommonFactory.*;
import static com.blue.verify.config.filter.BlueFilterOrder.BLUE_BODY_PROCESS_AND_DATA_REPORT;
import static java.lang.String.valueOf;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpHeaders.CONTENT_LENGTH;
import static org.springframework.http.HttpStatus.OK;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * data report filter
 *
 * @author DarkBlue
 */
@SuppressWarnings({"NullableProblems"})
@Component
public final class BlueBodyProcessAndDataReportFilter implements WebFilter, Ordered {

    private static final Logger LOGGER = getLogger(BlueBodyProcessAndDataReportFilter.class);

    private final RequestBodyProcessor requestBodyProcessor;

    private final RequestEventReporter requestEventReporter;

    public BlueBodyProcessAndDataReportFilter(RequestBodyProcessor requestBodyProcessor, RequestEventReporter requestEventReporter, ResponseDeploy responseDeploy) {
        this.requestBodyProcessor = requestBodyProcessor;
        this.requestEventReporter = requestEventReporter;
        this.existenceBodyResponseContentTypes = new HashSet<>(responseDeploy.getExistenceBodyTypes());
    }

    private Set<String> existenceBodyResponseContentTypes;

    private final Function<HttpHeaders, Boolean> EXISTENCE_BODY_PREDICATE = httpHeaders ->
            existenceBodyResponseContentTypes.contains(HEADER_VALUE_GETTER.apply(httpHeaders, HttpHeaders.CONTENT_TYPE));

    private void reportError(Throwable throwable, ServerHttpRequest request, RequestEventReporter requestEventReporter, DataEvent dataEvent) {
        ExceptionResponse exceptionResponse = THROWABLE_CONVERTER.apply(throwable, getAcceptLanguages(request));

        dataEvent.addData(RESPONSE_STATUS.key, valueOf(exceptionResponse.getStatus()).intern());
        dataEvent.addData(RESPONSE_BODY.key, GSON.toJson(exceptionResponse));

        LOGGER.info("getResponseAndReport(), dataEvent = {}, throwable = {}", dataEvent, throwable);
        requestEventReporter.report(dataEvent);
    }

    private void packageRequestInfo(DataEvent dataEvent, ServerWebExchange exchange) {

        Map<String, Object> attributes = exchange.getAttributes();

        dataEvent.addData(REQUEST_ID.key, valueOf(attributes.get(REQUEST_ID.key)));

        dataEvent.setDataEventType(UNIFIED);
        dataEvent.setStamp(TIME_STAMP_GETTER.get());

        ofNullable(attributes.get(METADATA.key)).map(String::valueOf)
                .ifPresent(metadata -> dataEvent.addData(METADATA.key, metadata));
        ofNullable(attributes.get(JWT.key)).map(String::valueOf)
                .ifPresent(jwt -> dataEvent.addData(JWT.key, jwt));
        ofNullable(attributes.get(METHOD.key)).map(String::valueOf)
                .ifPresent(access -> dataEvent.addData(ACCESS.key, access));
        ofNullable(attributes.get(URI.key)).map(String::valueOf)
                .ifPresent(access -> dataEvent.addData(URI.key, access));
        ofNullable(attributes.get(REAL_URI.key)).map(String::valueOf)
                .ifPresent(access -> dataEvent.addData(REAL_URI.key, access));
        ofNullable(attributes.get(CLIENT_IP.key)).map(String::valueOf)
                .ifPresent(access -> dataEvent.addData(CLIENT_IP.key, access));
    }

    private Mono<String> getResponseBodyAndReport(ServerHttpResponse response, HttpStatus httpStatus, Publisher<? extends DataBuffer> body, DataEvent dataEvent) {
        return ClientResponse
                .create(httpStatus)
                .headers(hs ->
                        hs.putAll(response.getHeaders()))
                .body(Flux.from(body)).build()
                .bodyToMono(String.class)
                .flatMap(responseBody -> {
                            dataEvent.addData(RESPONSE_BODY.key, responseBody);
                            LOGGER.info("getBodyWithPackageResponse(), dataEvent = {}", dataEvent);
                            requestEventReporter.report(dataEvent);

                            response.getHeaders().put(CONTENT_LENGTH, singletonList(valueOf(responseBody.getBytes(UTF_8).length)));
                            return just(responseBody);
                        }
                );
    }

    private ServerHttpResponse getResponseAndReport(ServerWebExchange exchange, DataEvent dataEvent) {
        ServerHttpResponse response = exchange.getResponse();
        HttpStatus httpStatus = ofNullable(response.getStatusCode()).orElse(OK);
        dataEvent.addData(RESPONSE_STATUS.key, valueOf(httpStatus.value()).intern());

        if (EXISTENCE_BODY_PREDICATE.apply(response.getHeaders())) {
            return new ServerHttpResponseDecorator(response) {
                @Override
                public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                    return getResponseBodyAndReport(response, httpStatus, body, dataEvent)
                            .flatMap(data -> {
                                byte[] bytes = data.getBytes(UTF_8);
                                DataBuffer resBuffer = DATA_BUFFER_FACTORY.allocateBuffer(bytes.length);
                                resBuffer.write(bytes);
                                response.getHeaders().put(CONTENT_LENGTH, singletonList(valueOf(bytes.length)));

                                return getDelegate().writeWith(just(resBuffer));
                            });
                }

                @Override
                public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
                    return getDelegate().writeWith(Flux.from(body).flatMapSequential(p -> p));
                }
            };
        }

        LOGGER.info("getBodyWithPackageResponse(), dataEvent = {}", dataEvent);
        requestEventReporter.report(dataEvent);

        return response;
    }

    public Mono<Void> reportWithRequestBody(ServerHttpRequest request, ServerWebExchange exchange, WebFilterChain chain, DataEvent dataEvent) {

        packageRequestInfo(dataEvent, exchange);

        return ServerRequest.create(exchange, MESSAGE_READERS)
                .bodyToMono(String.class)
                .switchIfEmpty(
                        just(""))
                .flatMap(requestBody -> {
                    dataEvent.addData(REQUEST_BODY.key, requestBody);
                    return just(REQUEST_DECORATOR_GENERATOR.apply(request, just(requestBodyProcessor.handleRequestBody(requestBody))));
                })
                .flatMap(decorator ->
                        chain.filter(
                                        exchange.mutate()
                                                .request(decorator)
                                                .response(getResponseAndReport(exchange, dataEvent))
                                                .build())
                                .doOnError(throwable -> {
                                    ON_ERROR_CONSUMER.accept(throwable, decorator);
                                    reportError(throwable, request, requestEventReporter, dataEvent);
                                })
                );
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        DataEvent dataEvent = new DataEvent();

        return reportWithRequestBody(exchange.getRequest(), exchange, chain, dataEvent);
    }

    @Override
    public int getOrder() {
        return BLUE_BODY_PROCESS_AND_DATA_REPORT.order;
    }

}

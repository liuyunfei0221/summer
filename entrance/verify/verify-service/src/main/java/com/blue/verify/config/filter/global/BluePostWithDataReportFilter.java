package com.blue.verify.config.filter.global;

import com.blue.base.common.content.common.RequestBodyProcessor;
import com.blue.base.model.common.DataEvent;
import com.blue.base.model.common.ExceptionResponse;
import com.blue.verify.component.event.RequestEventReporter;
import com.blue.verify.config.deploy.ResponseDeploy;
import org.reactivestreams.Publisher;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.HttpMessageReader;
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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static com.blue.base.common.base.CommonFunctions.*;
import static com.blue.base.constant.common.BlueDataAttrKey.*;
import static com.blue.base.constant.common.DataEventOpType.CLICK;
import static com.blue.base.constant.common.DataEventType.UNIFIED;
import static com.blue.base.constant.common.SpecialStringElement.EMPTY_DATA;
import static com.blue.verify.common.VerifyCommonFactory.*;
import static com.blue.verify.config.filter.BlueFilterOrder.BLUE_POST_WITH_DATA_REPORT;
import static java.lang.String.valueOf;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpHeaders.CONTENT_LENGTH;
import static org.springframework.http.HttpStatus.OK;
import static reactor.core.publisher.Mono.defer;
import static reactor.core.publisher.Mono.just;

/**
 * data report filter
 *
 * @author liuyunfei
 */
@SuppressWarnings({"NullableProblems"})
@Component
public final class BluePostWithDataReportFilter implements WebFilter, Ordered {

    private final List<HttpMessageReader<?>> httpMessageReaders;

    private final RequestBodyProcessor requestBodyProcessor;

    private final RequestEventReporter requestEventReporter;

    public BluePostWithDataReportFilter(List<HttpMessageReader<?>> httpMessageReaders, RequestBodyProcessor requestBodyProcessor, RequestEventReporter requestEventReporter, ResponseDeploy responseDeploy) {
        this.httpMessageReaders = httpMessageReaders;
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

        requestEventReporter.report(dataEvent);
    }

    private void packageRequestInfo(DataEvent dataEvent, Map<String, Object> attributes) {
        dataEvent.setDataEventType(UNIFIED);
        dataEvent.setDataEventOpType(CLICK);

        dataEvent.setStamp(TIME_STAMP_GETTER.get());

        EVENT_PACKAGER.accept(attributes, dataEvent);
    }

    private Mono<String> getResponseBodyAndReport(ServerHttpResponse response, HttpStatus responseHttpStatus, Publisher<? extends DataBuffer> body, DataEvent dataEvent) {
        return ClientResponse
                .create(responseHttpStatus, httpMessageReaders)
                .headers(hs ->
                        hs.putAll(response.getHeaders()))
                .body(Flux.from(body)).build()
                .bodyToMono(String.class)
                .flatMap(responseBody -> {
                            dataEvent.addData(RESPONSE_BODY.key, responseBody);
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

        requestEventReporter.report(dataEvent);

        return response;
    }

    private Mono<Void> reportWithoutRequestBody(ServerWebExchange exchange, WebFilterChain chain, DataEvent dataEvent) {
        packageRequestInfo(dataEvent, exchange.getAttributes());
        return chain.filter(
                exchange.mutate().response(
                        getResponseAndReport(exchange, dataEvent)
                ).build());
    }

    private Mono<Void> reportWithRequestBody(ServerHttpRequest request, ServerWebExchange exchange, WebFilterChain chain, DataEvent dataEvent) {

        packageRequestInfo(dataEvent, exchange.getAttributes());

        return ServerRequest.create(exchange, httpMessageReaders)
                .bodyToMono(String.class)
                .switchIfEmpty(defer(() -> just(EMPTY_DATA.value)))
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

        if (ofNullable(exchange.getAttributes().get(EXISTENCE_REQUEST_BODY.key))
                .map(b -> (boolean) b).orElse(true)) {
            return reportWithRequestBody(exchange.getRequest(), exchange, chain, dataEvent);
        } else {
            return reportWithoutRequestBody(exchange, chain, dataEvent);
        }
    }

    @Override
    public int getOrder() {
        return BLUE_POST_WITH_DATA_REPORT.order;
    }

}

package com.blue.gateway.config.filter.global;

import com.blue.basic.common.content.common.RequestBodyProcessor;
import com.blue.basic.constant.common.BlueHeader;
import com.blue.basic.model.common.ExceptionElement;
import com.blue.basic.model.event.DataEvent;
import com.blue.gateway.component.event.RequestEventReporter;
import com.blue.gateway.config.deploy.EncryptDeploy;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
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
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import static com.blue.basic.common.base.CommonFunctions.*;
import static com.blue.basic.constant.common.BlueDataAttrKey.*;
import static com.blue.basic.constant.common.DataEventOpType.CLICK;
import static com.blue.basic.constant.common.DataEventType.UNIFIED;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_DATA;
import static com.blue.gateway.common.GatewayCommonFunctions.ON_ERROR_CONSUMER_WITH_MESSAGE;
import static com.blue.gateway.common.GatewayCommonFunctions.getRequestDecorator;
import static com.blue.gateway.config.filter.BlueFilterOrder.BLUE_POST_WITH_DATA_REPORT;
import static java.lang.String.valueOf;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpHeaders.CONTENT_LENGTH;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;
import static reactor.core.publisher.Flux.from;
import static reactor.core.publisher.Mono.defer;
import static reactor.core.publisher.Mono.just;

/**
 * data report filter
 *
 * @author liuyunfei
 */
@Component
public final class BluePostWithDataReportFilter implements GlobalFilter, Ordered {

    private final List<HttpMessageReader<?>> httpMessageReaders;

    private final RequestBodyProcessor requestBodyProcessor;

    private final RequestEventReporter requestEventReporter;

    public BluePostWithDataReportFilter(List<HttpMessageReader<?>> httpMessageReaders, RequestBodyProcessor requestBodyProcessor, RequestEventReporter requestEventReporter, EncryptDeploy encryptDeploy) {
        this.httpMessageReaders = httpMessageReaders;
        this.requestBodyProcessor = requestBodyProcessor;
        this.requestEventReporter = requestEventReporter;

        EXPIRED_SECONDS = encryptDeploy.getExpire();
    }

    private static long EXPIRED_SECONDS;

    private static final BiFunction<String, Map<String, Object>, String> REQUEST_BODY_PROCESSOR = (requestBody, attributes) ->
            ofNullable(attributes.get(REQUEST_UN_DECRYPTION.key))
                    .map(b -> (boolean) b).orElse(true) ?
                    requestBody
                    :
                    decryptRequestBody(requestBody,
                            ofNullable(attributes.get(SEC_KEY.key)).map(String::valueOf).orElse(EMPTY_DATA.value), EXPIRED_SECONDS);

    private static final BiFunction<String, Map<String, Object>, String> RESPONSE_BODY_PROCESSOR = (responseBody, attributes) ->
            ofNullable(attributes.get(RESPONSE_UN_ENCRYPTION.key))
                    .map(b -> (boolean) b).orElse(true) ?
                    responseBody
                    :
                    encryptResponseBody(responseBody, ofNullable(attributes.get(SEC_KEY.key)).map(s -> (String) s).orElse(EMPTY_DATA.value));

    private void packageError(Throwable throwable, ServerHttpRequest request, DataEvent dataEvent) {
        ExceptionElement exceptionElement = THROWABLE_CONVERTER.apply(throwable, getAcceptLanguages(request));
        dataEvent.addData(RESPONSE_STATUS.key, valueOf(exceptionElement.getStatus()).intern());
        dataEvent.addData(RESPONSE_BODY.key, GSON.toJson(EXP_ELE_2_RESP.apply(exceptionElement)));
    }

    private void packageRequestInfo(DataEvent dataEvent, Map<String, Object> attributes) {
        dataEvent.setDataEventType(UNIFIED.identity);
        dataEvent.setDataEventOpType(CLICK.identity);
        dataEvent.setStamp(TIME_STAMP_GETTER.get());

        EVENT_ATTR_PACKAGER.accept(attributes, dataEvent);
    }

    private Mono<String> getResponseBodyAndReport(ServerWebExchange exchange, HttpStatus responseHttpStatus, Publisher<? extends DataBuffer> body, DataEvent dataEvent) {
        ServerHttpResponse response = exchange.getResponse();

        return ClientResponse
                .create(responseHttpStatus, httpMessageReaders)
                .headers(hs ->
                        hs.putAll(response.getHeaders()))
                .body(from(body)).build()
                .bodyToMono(String.class)
                .flatMap(responseBody -> {
                            String tarBody = RESPONSE_BODY_PROCESSOR.apply(responseBody, exchange.getAttributes());

                            dataEvent.addData(RESPONSE_BODY.key, responseBody);
                            requestEventReporter.report(dataEvent);

                            response.getHeaders().put(CONTENT_LENGTH, singletonList(valueOf(tarBody.getBytes(UTF_8).length)));
                            return just(tarBody);
                        }
                );
    }

    private ServerHttpResponse getResponseAndReport(ServerWebExchange exchange, DataEvent dataEvent) {
        ServerHttpResponse response = exchange.getResponse();

        HttpStatus httpStatus = ofNullable(response.getStatusCode()).orElse(OK);
        dataEvent.addData(RESPONSE_STATUS.key, valueOf(httpStatus.value()).intern());

        if (ofNullable(exchange.getAttributes().get(EXISTENCE_RESPONSE_BODY.key))
                .map(b -> (boolean) b).orElse(true)) {
            //noinspection NullableProblems
            return new ServerHttpResponseDecorator(response) {
                @Override
                public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                    CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(
                            exchange, exchange.getResponse().getHeaders());

                    ofNullable(response.getHeaders().getFirst(BlueHeader.RESPONSE_EXTRA.name))
                            .ifPresent(extra -> dataEvent.addData(RESPONSE_EXTRA.key, extra));

                    return fromPublisher(getResponseBodyAndReport(exchange, httpStatus, body, dataEvent), String.class)
                            .insert(outputMessage, new BodyInserterContext())
                            .then(defer(() ->
                                    getDelegate().writeWith(outputMessage.getBody())))
                            .doOnError(throwable -> {
                                packageError(throwable, exchange.getRequest(), dataEvent);
                                ON_ERROR_CONSUMER_WITH_MESSAGE.accept(throwable, outputMessage);
                            });
                }

                @Override
                public Mono<Void> writeAndFlushWith(
                        Publisher<? extends Publisher<? extends DataBuffer>> body) {
                    return writeWith(from(body).flatMapSequential(p -> p));
                }
            };
        }

        ServerHttpResponse decoratorResponse = new ServerHttpResponseDecorator(response);
        ofNullable(response.getHeaders().getFirst(BlueHeader.RESPONSE_EXTRA.name))
                .ifPresent(extra -> dataEvent.addData(RESPONSE_EXTRA.key, extra));

        requestEventReporter.report(dataEvent);

        return decoratorResponse;
    }

    private Mono<Void> reportWithoutRequestBody(ServerWebExchange exchange, GatewayFilterChain chain, DataEvent dataEvent) {
        packageRequestInfo(dataEvent, exchange.getAttributes());
        return chain.filter(
                exchange.mutate().response(
                        getResponseAndReport(exchange, dataEvent)
                ).build());
    }

    private Mono<Void> reportWithRequestBody(ServerHttpRequest request, ServerWebExchange exchange, GatewayFilterChain chain, DataEvent dataEvent) {
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(request.getHeaders());

        Map<String, Object> attributes = exchange.getAttributes();
        CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(
                exchange, headers);

        packageRequestInfo(dataEvent, attributes);
        return fromPublisher(
                ServerRequest.create(exchange, httpMessageReaders)
                        .bodyToMono(String.class)
                        .switchIfEmpty(defer(() -> just(EMPTY_DATA.value)))
                        .flatMap(requestBody -> {
                            String tarBody = REQUEST_BODY_PROCESSOR.apply(requestBody, attributes);
                            dataEvent.addData(REQUEST_BODY.key, tarBody);
                            return just(requestBodyProcessor.handleRequestBody(tarBody));
                        }), String.class)
                .insert(outputMessage, new BodyInserterContext())
                .then(defer(() -> chain.filter(
                        exchange.mutate()
                                .request(getRequestDecorator(request, headers, outputMessage))
                                .response(getResponseAndReport(exchange, dataEvent))
                                .build())
                ))
                .doOnError(throwable -> {
                    packageError(throwable, request, dataEvent);
                    ON_ERROR_CONSUMER_WITH_MESSAGE.accept(throwable, outputMessage);
                });
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
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

package com.blue.gateway.config.filter.global;

import com.blue.base.common.base.CommonFunctions;
import com.blue.base.common.content.common.RequestBodyProcessor;
import com.blue.base.component.exception.handler.model.ExceptionHandleInfo;
import com.blue.base.model.base.DataEvent;
import com.blue.gateway.common.GatewayCommonFactory;
import com.blue.gateway.component.RequestEventReporter;
import com.blue.gateway.config.deploy.EncryptDeploy;
import com.google.gson.Gson;
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
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.blue.base.common.base.CommonFunctions.decryptRequestBody;
import static com.blue.base.common.base.CommonFunctions.encryptResponseBody;
import static com.blue.base.constant.base.BlueDataAttrKey.*;
import static com.blue.base.constant.base.DataEventType.UNIFIED;
import static com.blue.gateway.common.GatewayCommonFactory.getRequestDecorator;
import static com.blue.gateway.config.filter.BlueFilterOrder.BLUE_BODY_PROCESS_AND_DATA_REPORT;
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
import static reactor.util.Loggers.getLogger;

/**
 * data report filter
 *
 * @author DarkBlue
 */
@Component
public final class BlueBodyProcessAndDataReportFilter implements GlobalFilter, Ordered {

    private static final Logger LOGGER = getLogger(BlueBodyProcessAndDataReportFilter.class);

    private final RequestBodyProcessor requestBodyProcessor;

    private final RequestEventReporter requestEventReporter;

    private final EncryptDeploy encryptDeploy;

    public BlueBodyProcessAndDataReportFilter(RequestBodyProcessor requestBodyProcessor, RequestEventReporter requestEventReporter, EncryptDeploy encryptDeploy) {
        this.requestBodyProcessor = requestBodyProcessor;
        this.requestEventReporter = requestEventReporter;
        this.encryptDeploy = encryptDeploy;
    }

    private static final List<HttpMessageReader<?>> MESSAGE_READERS = GatewayCommonFactory.MESSAGE_READERS;

    public static final BiConsumer<Throwable, CachedBodyOutputMessage> ON_ERROR_CONSUMER_WITH_MESSAGE = GatewayCommonFactory.ON_ERROR_CONSUMER_WITH_MESSAGE;

    private static final Supplier<Long> TIME_STAMP_GETTER = GatewayCommonFactory.TIME_STAMP_GETTER;

    private static final Gson GSON = CommonFunctions.GSON;

    private static final Function<Throwable, ExceptionHandleInfo> THROWABLE_CONVERTER = GatewayCommonFactory.THROWABLE_CONVERTER;

    private static long EXPIRED_SECONDS;

    private static final BiFunction<String, Map<String, Object>, String> REQUEST_BODY_PROCESSOR = (requestBody, attributes) ->
            ofNullable(attributes.get(REQUEST_UN_DECRYPTION.key))
                    .map(b -> (boolean) b).orElse(true) ?
                    requestBody
                    :
                    decryptRequestBody(requestBody,
                            ofNullable(attributes.get(SEC_KEY.key)).map(String::valueOf).orElse(""),
                            EXPIRED_SECONDS);

    private static final BiFunction<String, Map<String, Object>, String> RESPONSE_BODY_PROCESSOR = (responseBody, attributes) ->
            ofNullable(attributes.get(RESPONSE_UN_ENCRYPTION.key))
                    .map(b -> (boolean) b).orElse(true) ?
                    responseBody
                    :
                    encryptResponseBody(responseBody, ofNullable(attributes.get(SEC_KEY.key)).map(s -> (String) s).orElse(""));

    private void packageError(Throwable throwable, DataEvent dataEvent) {
        ExceptionHandleInfo exceptionHandleInfo = THROWABLE_CONVERTER.apply(throwable);
        dataEvent.addData(RESPONSE_STATUS.key, valueOf(exceptionHandleInfo.getCode()).intern());
        dataEvent.addData(RESPONSE_BODY.key, GSON.toJson(exceptionHandleInfo.getBlueVo()));
    }

    @SuppressWarnings("DuplicatedCode")
    private void packageRequestInfo(DataEvent dataEvent, ServerHttpRequest request, ServerWebExchange exchange) {
        Map<String, Object> attributes = exchange.getAttributes();

        dataEvent.addData(REQUEST_ID.key, valueOf(attributes.get(REQUEST_ID.key)));

        dataEvent.setDataEventType(UNIFIED);
        dataEvent.setStamp(TIME_STAMP_GETTER.get());

        ofNullable(attributes.get(METADATA.key)).map(String::valueOf)
                .ifPresent(metadata -> dataEvent.addData(METADATA.key, metadata));
        ofNullable(attributes.get(JWT.key)).map(String::valueOf)
                .ifPresent(jwt -> dataEvent.addData(JWT.key, jwt));
        ofNullable(attributes.get(ACCESS.key)).map(String::valueOf)
                .ifPresent(access -> dataEvent.addData(ACCESS.key, access));

        dataEvent.addData(CLIENT_IP.key, ofNullable(attributes.get(CLIENT_IP.key)).map(String::valueOf).orElse(""));
        dataEvent.addData(METHOD.key, request.getMethodValue().intern());
        dataEvent.addData(URI.key, request.getURI().getRawPath().intern());
    }

    private Mono<String> getResponseBodyAndReport(ServerWebExchange exchange, Publisher<? extends DataBuffer> body, DataEvent dataEvent) {
        ServerHttpResponse response = exchange.getResponse();
        HttpStatus httpStatus = ofNullable(response.getStatusCode()).orElse(OK);
        dataEvent.addData(RESPONSE_STATUS.key, valueOf(httpStatus.value()).intern());

        return ClientResponse
                .create(httpStatus)
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
        if (ofNullable(exchange.getAttributes().get(EXISTENCE_RESPONSE_BODY.key))
                .map(b -> (boolean) b).orElse(true)) {
            //noinspection NullableProblems
            return new ServerHttpResponseDecorator(response) {
                @Override
                public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                    CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(
                            exchange, exchange.getResponse().getHeaders());
                    return fromPublisher(getResponseBodyAndReport(exchange, body, dataEvent), String.class)
                            .insert(outputMessage, new BodyInserterContext())
                            .then(defer(() ->
                                    getDelegate().writeWith(outputMessage.getBody())))
                            .doOnError(throwable -> {
                                packageError(throwable, dataEvent);
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

        LOGGER.info("getBodyWithPackageResponse(), dataEvent = {}", dataEvent);
        requestEventReporter.report(dataEvent);

        return response;
    }

    private Mono<Void> reportWithoutRequestBody(ServerHttpRequest request, ServerWebExchange exchange, GatewayFilterChain chain, DataEvent dataEvent) {
        packageRequestInfo(dataEvent, request, exchange);
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

        packageRequestInfo(dataEvent, request, exchange);
        return fromPublisher(
                ServerRequest.create(exchange, MESSAGE_READERS)
                        .bodyToMono(String.class)
                        .switchIfEmpty(
                                just(""))
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
                    packageError(throwable, dataEvent);
                    ON_ERROR_CONSUMER_WITH_MESSAGE.accept(throwable, outputMessage);
                });
    }

    /**
     * init
     */
    @PostConstruct
    private void init() {
        EXPIRED_SECONDS = encryptDeploy.getExpire();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        DataEvent dataEvent = new DataEvent();

        if (ofNullable(exchange.getAttributes().get(EXISTENCE_REQUEST_BODY.key))
                .map(b -> (boolean) b).orElse(true)) {
            return reportWithRequestBody(request, exchange, chain, dataEvent);
        } else {
            return reportWithoutRequestBody(request, exchange, chain, dataEvent);
        }
    }

    @Override
    public int getOrder() {
        return BLUE_BODY_PROCESS_AND_DATA_REPORT.order;
    }

}

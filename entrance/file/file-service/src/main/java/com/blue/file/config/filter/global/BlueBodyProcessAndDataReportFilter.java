package com.blue.file.config.filter.global;

import com.blue.base.common.content.common.RequestBodyProcessor;
import com.blue.base.model.base.DataEvent;
import com.blue.base.model.base.ExceptionResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.file.common.request.body.ReportWithRequestBodyProcessor;
import com.blue.file.component.RequestEventReporter;
import com.blue.file.config.deploy.EncryptDeploy;
import org.reactivestreams.Publisher;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.multipart.Part;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.blue.base.common.base.CommonFunctions.*;
import static com.blue.base.constant.base.BlueDataAttrKey.*;
import static com.blue.base.constant.base.DataEventType.UNIFIED;
import static com.blue.base.constant.base.ResponseElement.UNSUPPORTED_MEDIA_TYPE;
import static com.blue.file.common.FluxCommonFunctions.*;
import static com.blue.file.config.filter.BlueFilterOrder.BLUE_BODY_PROCESS_AND_DATA_REPORT;
import static java.lang.String.valueOf;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpHeaders.CONTENT_LENGTH;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * data report filter
 *
 * @author DarkBlue
 */
@SuppressWarnings({"NullableProblems", "AliControlFlowStatementWithoutBraces"})
@Component
public final class BlueBodyProcessAndDataReportFilter implements WebFilter, Ordered {

    private static final Logger LOGGER = getLogger(BlueBodyProcessAndDataReportFilter.class);

    private final RequestBodyProcessor requestBodyProcessor;

    private final RequestEventReporter requestEventReporter;

    public BlueBodyProcessAndDataReportFilter(RequestBodyProcessor requestBodyProcessor, RequestEventReporter requestEventReporter, EncryptDeploy encryptDeploy) {
        this.requestBodyProcessor = requestBodyProcessor;
        this.requestEventReporter = requestEventReporter;

        EXPIRED_SECONDS = encryptDeploy.getExpire();

        ReportWithRequestBodyProcessor jsonRequestBodyProcessor = new JsonRequestBodyProcessor();
        ReportWithRequestBodyProcessor multipartRequestBodyProcessor = new MultipartRequestBodyProcessor();

        REQUEST_BODY_PROCESSOR_HOLDER.put(jsonRequestBodyProcessor.getContentType(), jsonRequestBodyProcessor);
        REQUEST_BODY_PROCESSOR_HOLDER.put(multipartRequestBodyProcessor.getContentType(), multipartRequestBodyProcessor);
    }

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
        ofNullable(attributes.get(ACCESS.key)).map(String::valueOf)
                .ifPresent(access -> dataEvent.addData(ACCESS.key, access));
        ofNullable(attributes.get(METHOD.key)).map(String::valueOf)
                .ifPresent(access -> dataEvent.addData(ACCESS.key, access));
        ofNullable(attributes.get(URI.key)).map(String::valueOf)
                .ifPresent(access -> dataEvent.addData(URI.key, access));
        ofNullable(attributes.get(REAL_URI.key)).map(String::valueOf)
                .ifPresent(access -> dataEvent.addData(REAL_URI.key, access));
        ofNullable(attributes.get(CLIENT_IP.key)).map(String::valueOf)
                .ifPresent(access -> dataEvent.addData(CLIENT_IP.key, access));
        LOGGER.info("packageRequestParam(), dataEvent = {}", dataEvent);
    }

    private Mono<String> getResponseBodyAndReport(ServerWebExchange exchange, ServerHttpResponse response, HttpStatus httpStatus, Publisher<? extends DataBuffer> body, DataEvent dataEvent) {
        return ClientResponse
                .create(httpStatus)
                .headers(hs ->
                        hs.putAll(response.getHeaders()))
                .body(Flux.from(body)).build()
                .bodyToMono(String.class)
                .flatMap(responseBody -> {
                            String tarBody = RESPONSE_BODY_PROCESSOR.apply(responseBody, exchange.getAttributes());

                            dataEvent.addData(RESPONSE_BODY.key, responseBody);
                            LOGGER.info("getBodyWithPackageResponse(), dataEvent = {}", dataEvent);
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
            return new ServerHttpResponseDecorator(response) {
                @Override
                public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                    return getResponseBodyAndReport(exchange, response, httpStatus, body, dataEvent).flatMap(data -> {
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

    private Mono<Void> reportWithoutRequestBody(ServerWebExchange exchange, WebFilterChain chain, DataEvent dataEvent) {
        packageRequestInfo(dataEvent, exchange);
        return chain.filter(
                exchange.mutate().response(
                        getResponseAndReport(exchange, dataEvent)
                ).build());
    }

    private final Map<String, ReportWithRequestBodyProcessor> REQUEST_BODY_PROCESSOR_HOLDER = new HashMap<>(4, 1.0f);

    private final Function<HttpHeaders, ReportWithRequestBodyProcessor> REQUEST_BODY_PROCESSOR_GETTER = headers -> {
        ReportWithRequestBodyProcessor processor = REQUEST_BODY_PROCESSOR_HOLDER.get(HEADER_VALUE_GETTER.apply(headers, CONTENT_TYPE));

        if (processor == null)
            throw new BlueException(UNSUPPORTED_MEDIA_TYPE);

        return processor;
    };

    private Mono<Void> reportWithRequestBody(ServerHttpRequest request, ServerWebExchange exchange, WebFilterChain chain, DataEvent dataEvent) {
        packageRequestInfo(dataEvent, exchange);
        return REQUEST_BODY_PROCESSOR_GETTER.apply(request.getHeaders()).processor(request, exchange, chain, requestEventReporter, dataEvent);
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
        return BLUE_BODY_PROCESS_AND_DATA_REPORT.order;
    }


    protected class JsonRequestBodyProcessor implements ReportWithRequestBodyProcessor {

        @Override
        public String getContentType() {
            return APPLICATION_JSON_VALUE;
        }

        @Override
        public Mono<Void> processor(ServerHttpRequest request, ServerWebExchange exchange, WebFilterChain chain, RequestEventReporter requestEventReporter, DataEvent dataEvent) {
            return ServerRequest.create(exchange, MESSAGE_READERS)
                    .bodyToMono(String.class)
                    .switchIfEmpty(
                            just(""))
                    .flatMap(requestBody -> {
                        String tarBody = REQUEST_BODY_PROCESSOR.apply(requestBody, exchange.getAttributes());
                        dataEvent.addData(REQUEST_BODY.key, tarBody);
                        return just(REQUEST_DECORATOR_GENERATOR.apply(request, just(requestBodyProcessor.handleRequestBody(tarBody))));
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
    }

    protected class MultipartRequestBodyProcessor implements ReportWithRequestBodyProcessor {
        @Override
        public String getContentType() {
            return MULTIPART_FORM_DATA_VALUE;
        }

        @Override
        public Mono<Void> processor(ServerHttpRequest request, ServerWebExchange exchange, WebFilterChain chain, RequestEventReporter requestEventReporter, DataEvent dataEvent) {
            ServerWebExchangeDecorator serverWebExchangeDecorator = new ServerWebExchangeDecorator(exchange) {
                @Override
                public Mono<MultiValueMap<String, Part>> getMultipartData() {
                    return extractValuesToBind(exchange)
                            .flatMap(params -> {
                                dataEvent.addData(REQUEST_BODY.key, GSON.toJson(params));
                                return super.getMultipartData();
                            });
                }
            };

            return chain.filter(serverWebExchangeDecorator.mutate()
                            .response(
                                    getResponseAndReport(exchange, dataEvent)).build())
                    .doOnError(throwable ->
                            reportError(throwable, request, requestEventReporter, dataEvent));
        }
    }

}

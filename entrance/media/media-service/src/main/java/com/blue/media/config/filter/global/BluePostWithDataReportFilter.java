package com.blue.media.config.filter.global;

import com.blue.base.common.content.common.RequestBodyProcessor;
import com.blue.base.model.common.DataEvent;
import com.blue.base.model.common.ExceptionResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.media.component.event.RequestEventReporter;
import com.blue.media.config.deploy.EncryptDeploy;
import org.reactivestreams.Publisher;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.HttpMessageReader;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.blue.base.common.base.BlueChecker.isNull;
import static com.blue.base.common.base.CommonFunctions.*;
import static com.blue.base.constant.base.BlueDataAttrKey.*;
import static com.blue.base.constant.base.DataEventOpType.CLICK;
import static com.blue.base.constant.base.DataEventType.UNIFIED;
import static com.blue.base.constant.base.ResponseElement.UNSUPPORTED_MEDIA_TYPE;
import static com.blue.media.common.MediaCommonFunctions.*;
import static com.blue.media.config.filter.BlueFilterOrder.BLUE_POST_WITH_DATA_REPORT;
import static java.lang.String.valueOf;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpHeaders.CONTENT_LENGTH;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static reactor.core.publisher.Mono.defer;
import static reactor.core.publisher.Mono.just;

/**
 * data report filter
 *
 * @author liuyunfei
 */
@SuppressWarnings({"NullableProblems", "AliControlFlowStatementWithoutBraces"})
@Component
public final class BluePostWithDataReportFilter implements WebFilter, Ordered {

    private final List<HttpMessageReader<?>> httpMessageReaders;

    private final RequestBodyProcessor requestBodyProcessor;

    private final RequestEventReporter requestEventReporter;

    public BluePostWithDataReportFilter(List<HttpMessageReader<?>> httpMessageReaders, RequestBodyProcessor requestBodyProcessor, RequestEventReporter requestEventReporter, EncryptDeploy encryptDeploy) {
        this.httpMessageReaders = httpMessageReaders;
        this.requestBodyProcessor = requestBodyProcessor;
        this.requestEventReporter = requestEventReporter;

        EXPIRED_SECONDS = encryptDeploy.getExpire();

        RequestBodyReporter jsonRequestBodyProcessor = new JsonRequestBodyReporter();
        RequestBodyReporter multipartRequestBodyProcessor = new MultipartRequestBodyReporter();

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

        requestEventReporter.report(dataEvent);
    }

    private void packageRequestInfo(DataEvent dataEvent, Map<String, Object> attributes) {
        dataEvent.setDataEventType(UNIFIED);
        dataEvent.setDataEventOpType(CLICK);

        dataEvent.setStamp(TIME_STAMP_GETTER.get());

        EVENT_PACKAGER.accept(attributes, dataEvent);
    }

    private Mono<String> getResponseBodyAndReport(ServerWebExchange exchange, ServerHttpResponse response, HttpStatus responseHttpStatus, Publisher<? extends DataBuffer> body, DataEvent dataEvent) {
        return ClientResponse
                .create(responseHttpStatus, httpMessageReaders)
                .headers(hs ->
                        hs.putAll(response.getHeaders()))
                .body(Flux.from(body)).build()
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

    private final Map<String, RequestBodyReporter> REQUEST_BODY_PROCESSOR_HOLDER = new HashMap<>(4, 1.0f);

    private final Function<HttpHeaders, RequestBodyReporter> REQUEST_BODY_PROCESSOR_GETTER = headers -> {
        RequestBodyReporter reporter = REQUEST_BODY_PROCESSOR_HOLDER.get(HEADER_VALUE_GETTER.apply(headers, CONTENT_TYPE));

        if (isNull(reporter))
            throw new BlueException(UNSUPPORTED_MEDIA_TYPE);

        return reporter;
    };

    private Mono<Void> reportWithRequestBody(ServerHttpRequest request, ServerWebExchange exchange, WebFilterChain chain, DataEvent dataEvent) {
        packageRequestInfo(dataEvent, exchange.getAttributes());
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
        return BLUE_POST_WITH_DATA_REPORT.order;
    }


    /**
     * reporter
     */
    @SuppressWarnings("JavaDoc")
    protected interface RequestBodyReporter {

        /**
         * handle type
         *
         * @return
         */
        String getContentType();

        /**
         * handle
         *
         * @param request
         * @param exchange
         * @param chain
         * @param requestEventReporter
         * @param dataEvent
         * @return
         */
        Mono<Void> processor(ServerHttpRequest request, ServerWebExchange exchange, WebFilterChain chain, RequestEventReporter requestEventReporter, DataEvent dataEvent);

    }

    /**
     * impl for json
     */
    @SuppressWarnings("JavaDoc")
    protected class JsonRequestBodyReporter implements RequestBodyReporter {

        /**
         * handle type
         *
         * @return
         */
        @Override
        public String getContentType() {
            return APPLICATION_JSON_VALUE;
        }

        /**
         * handle
         *
         * @param request
         * @param exchange
         * @param chain
         * @param requestEventReporter
         * @param dataEvent
         * @return
         */
        @Override
        public Mono<Void> processor(ServerHttpRequest request, ServerWebExchange exchange, WebFilterChain chain, RequestEventReporter requestEventReporter, DataEvent dataEvent) {
            return ServerRequest.create(exchange, httpMessageReaders)
                    .bodyToMono(String.class)
                    .switchIfEmpty(defer(() -> just("")))
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

    /**
     * impl for part
     */
    @SuppressWarnings("JavaDoc")
    protected class MultipartRequestBodyReporter implements RequestBodyReporter {

        /**
         * handle type
         *
         * @return
         */
        @Override
        public String getContentType() {
            return MULTIPART_FORM_DATA_VALUE;
        }

        /**
         * handle
         *
         * @param request
         * @param exchange
         * @param chain
         * @param requestEventReporter
         * @param dataEvent
         * @return
         */
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

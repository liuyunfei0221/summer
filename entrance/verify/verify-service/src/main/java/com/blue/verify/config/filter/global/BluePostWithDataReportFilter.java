package com.blue.verify.config.filter.global;

import com.blue.basic.common.content.common.StringProcessor;
import com.blue.basic.constant.common.BlueHeader;
import com.blue.basic.model.common.ExceptionElement;
import com.blue.basic.model.event.DataEvent;
import com.blue.basic.model.exps.BlueException;
import com.blue.verify.component.event.RequestEventReporter;
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
import java.util.function.BiConsumer;
import java.util.function.Function;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.common.base.CommonFunctions.*;
import static com.blue.basic.constant.common.BlueDataAttrKey.*;
import static com.blue.basic.constant.common.DataEventOpType.CLICK;
import static com.blue.basic.constant.common.DataEventType.UNIFIED;
import static com.blue.basic.constant.common.ResponseElement.UNSUPPORTED_MEDIA_TYPE;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_VALUE;
import static com.blue.verify.common.VerifyCommonFactory.*;
import static com.blue.verify.config.filter.BlueFilterOrder.BLUE_POST_WITH_DATA_REPORT;
import static java.lang.String.valueOf;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.web.bind.support.WebExchangeDataBinder.extractValuesToBind;
import static reactor.core.publisher.Flux.from;
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

    private final StringProcessor stringProcessor;

    private final RequestEventReporter requestEventReporter;

    public BluePostWithDataReportFilter(List<HttpMessageReader<?>> httpMessageReaders, StringProcessor stringProcessor, RequestEventReporter requestEventReporter) {
        this.httpMessageReaders = httpMessageReaders;
        this.stringProcessor = stringProcessor;
        this.requestEventReporter = requestEventReporter;

        RequestBodyHandler jsonRequestBodyProcessor = new JsonRequestBodyHandler();
        RequestBodyHandler multipartRequestBodyProcessor = new MultipartRequestBodyHandler();

        REQUEST_BODY_HANDLER_HOLDER.put(jsonRequestBodyProcessor.getContentType(), jsonRequestBodyProcessor);
        REQUEST_BODY_HANDLER_HOLDER.put(multipartRequestBodyProcessor.getContentType(), multipartRequestBodyProcessor);
    }

    private void packageError(Throwable throwable, ServerHttpRequest request, DataEvent dataEvent) {
        ExceptionElement exceptionElement = THROWABLE_CONVERTER.apply(throwable, getAcceptLanguages(request));
        dataEvent.addData(RESPONSE_STATUS.key, valueOf(exceptionElement.getStatus()).intern());
        dataEvent.addData(RESPONSE_BODY.key, GSON.toJson(EXP_ELE_2_RESP.apply(exceptionElement)));
    }

    private static final BiConsumer<Map<String, Object>, DataEvent> REQUEST_INFO_PACKAGER = (attributes, dataEvent) -> {
        dataEvent.setDataEventType(UNIFIED.identity);
        dataEvent.setDataEventOpType(CLICK.identity);
        dataEvent.setStamp(TIME_STAMP_GETTER.get());

        EVENT_ATTR_PACKAGER.accept(attributes, dataEvent);
    };

    private Mono<String> getResponseBody(ServerHttpResponse response, HttpStatus responseHttpStatus, Publisher<? extends DataBuffer> body, DataEvent dataEvent) {
        return ClientResponse
                .create(responseHttpStatus, httpMessageReaders)
                .headers(hs -> {
                    HttpHeaders headers = response.getHeaders();
                    hs.putAll(headers);
                    ofNullable(headers.getFirst(BlueHeader.RESPONSE_EXTRA.name))
                            .ifPresent(extra -> dataEvent.addData(RESPONSE_EXTRA.key, extra));
                })
                .body(Flux.from(body)).build()
                .bodyToMono(String.class)
                .flatMap(responseBody -> {
                            dataEvent.addData(RESPONSE_BODY.key, responseBody);
                            return just(responseBody);
                        }
                );
    }

    private ServerHttpResponse getResponse(ServerWebExchange exchange, DataEvent dataEvent) {
        ServerHttpResponse response = exchange.getResponse();

        return ofNullable(exchange.getAttributes().get(EXISTENCE_RESPONSE_BODY.key))
                .map(b -> (boolean) b).orElse(true) ?
                new ServerHttpResponseDecorator(response) {

                    @Override
                    public HttpStatus getStatusCode() {
                        HttpStatus httpStatus = ofNullable(super.getStatusCode()).orElse(OK);
                        dataEvent.addData(RESPONSE_STATUS.key, valueOf(httpStatus.value()).intern());
                        return httpStatus;
                    }

                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        return getResponseBody(response, this.getStatusCode(), body, dataEvent).flatMap(data -> {
                            byte[] bytes = data.getBytes(UTF_8);
                            DataBuffer resBuffer = DATA_BUFFER_FACTORY.allocateBuffer(bytes.length);
                            resBuffer.write(bytes);

                            return getDelegate().writeWith(just(resBuffer));
                        });
                    }

                    @Override
                    public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
                        return getDelegate().writeWith(Flux.from(body).flatMapSequential(p -> p));
                    }
                }
                :
                new ServerHttpResponseDecorator(response) {
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        dataEvent.addData(RESPONSE_STATUS.key, valueOf(ofNullable(super.getStatusCode()).orElse(OK).value()).intern());
                        return super.writeWith(body);
                    }

                    @Override
                    public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
                        return writeWith(from(body).flatMapSequential(p -> p));
                    }
                };
    }

    private Mono<Void> reportWithoutRequestBody(ServerWebExchange exchange, WebFilterChain chain) {
        DataEvent dataEvent = new DataEvent();

        REQUEST_INFO_PACKAGER.accept(exchange.getAttributes(), dataEvent);
        return chain.filter(exchange.mutate().response(getResponse(exchange, dataEvent)).build())
                .doOnSuccess(ig -> requestEventReporter.report(dataEvent))
                .doOnError(throwable -> {
                    packageError(throwable, exchange.getRequest(), dataEvent);
                    requestEventReporter.report(dataEvent);
                });
    }

    private final Map<String, RequestBodyHandler> REQUEST_BODY_HANDLER_HOLDER = new HashMap<>(4, 2.0f);

    private final Function<HttpHeaders, RequestBodyHandler> REQUEST_BODY_HANDLER_GETTER = headers -> {
        RequestBodyHandler handler = REQUEST_BODY_HANDLER_HOLDER.get(HEADER_VALUE_GETTER.apply(headers, CONTENT_TYPE));

        if (isNull(handler))
            throw new BlueException(UNSUPPORTED_MEDIA_TYPE);

        return handler;
    };

    private Mono<Void> reportWithRequestBody(ServerHttpRequest request, ServerWebExchange exchange, WebFilterChain chain) {
        DataEvent dataEvent = new DataEvent();

        REQUEST_INFO_PACKAGER.accept(exchange.getAttributes(), dataEvent);
        return REQUEST_BODY_HANDLER_GETTER.apply(request.getHeaders())
                .handle(request, exchange, chain, dataEvent)
                .doOnSuccess(ig -> requestEventReporter.report(dataEvent))
                .doOnError(throwable -> {
                    packageError(throwable, exchange.getRequest(), dataEvent);
                    requestEventReporter.report(dataEvent);
                });
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return ofNullable(exchange.getAttributes().get(EXISTENCE_REQUEST_BODY.key))
                .map(b -> (boolean) b).orElse(true) ?
                reportWithRequestBody(exchange.getRequest(), exchange, chain)
                :
                reportWithoutRequestBody(exchange, chain);
    }

    @Override
    public int getOrder() {
        return BLUE_POST_WITH_DATA_REPORT.order;
    }
    
    /**
     * reporter
     */
    @SuppressWarnings("JavaDoc")
    protected interface RequestBodyHandler {

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
         * @param dataEvent
         * @return
         */
        Mono<Void> handle(ServerHttpRequest request, ServerWebExchange exchange, WebFilterChain chain, DataEvent dataEvent);

    }

    /**
     * impl for json
     */
    @SuppressWarnings("JavaDoc")
    protected class JsonRequestBodyHandler implements RequestBodyHandler {

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
         * @param dataEvent
         * @return
         */
        @Override
        public Mono<Void> handle(ServerHttpRequest request, ServerWebExchange exchange, WebFilterChain chain, DataEvent dataEvent) {
            return ServerRequest.create(exchange, httpMessageReaders)
                    .bodyToMono(String.class)
                    .switchIfEmpty(defer(() -> just(EMPTY_VALUE.value)))
                    .flatMap(requestBody -> {
                        dataEvent.addData(REQUEST_BODY.key, requestBody);
                        return just(REQUEST_DECORATOR_GENERATOR.apply(request, just(stringProcessor.handle(requestBody))));
                    })
                    .flatMap(decorator ->
                            chain.filter(exchange.mutate().request(decorator)
                                            .response(getResponse(exchange, dataEvent)).build())
                                    .doOnError(throwable -> {
                                        ON_ERROR_CONSUMER.accept(throwable, decorator);
                                        packageError(throwable, request, dataEvent);
                                    })
                    );
        }
    }

    /**
     * impl for part
     */
    @SuppressWarnings("JavaDoc")
    protected class MultipartRequestBodyHandler implements RequestBodyHandler {

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
         * @param dataEvent
         * @return
         */
        @Override
        public Mono<Void> handle(ServerHttpRequest request, ServerWebExchange exchange, WebFilterChain chain, DataEvent dataEvent) {
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

            return chain.filter(serverWebExchangeDecorator.mutate().response(getResponse(exchange, dataEvent)).build())
                    .doOnError(throwable -> packageError(throwable, request, dataEvent));
        }
    }

}

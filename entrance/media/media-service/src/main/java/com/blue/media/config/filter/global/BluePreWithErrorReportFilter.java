package com.blue.media.config.filter.global;

import com.blue.basic.constant.common.BlueHeader;
import com.blue.basic.model.common.ExceptionElement;
import com.blue.basic.model.event.DataEvent;
import com.blue.basic.model.exps.BlueException;
import com.blue.media.component.event.RequestEventReporter;
import com.blue.media.config.deploy.ErrorReportDeploy;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.common.base.CommonFunctions.*;
import static com.blue.basic.constant.common.BlueDataAttrKey.*;
import static com.blue.basic.constant.common.BlueHeader.AUTHORIZATION;
import static com.blue.basic.constant.common.BlueHeader.REQUEST_IP;
import static com.blue.basic.constant.common.DataEventOpType.CLICK;
import static com.blue.basic.constant.common.DataEventType.UNIFIED;
import static com.blue.basic.constant.common.ResponseElement.UNSUPPORTED_MEDIA_TYPE;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_VALUE;
import static com.blue.media.common.MediaCommonFunctions.extractValuesToBind;
import static com.blue.media.config.filter.BlueFilterOrder.BLUE_PRE_WITH_ERROR_REPORT;
import static java.lang.String.valueOf;
import static java.util.Optional.ofNullable;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
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

    public BluePreWithErrorReportFilter(List<HttpMessageReader<?>> httpMessageReaders, ExecutorService executorService, RequestEventReporter requestEventReporter, ErrorReportDeploy errorReportDeploy) {
        this.httpMessageReaders = httpMessageReaders;
        this.executorService = executorService;
        this.requestEventReporter = requestEventReporter;
        withRequestBodyContentTypes = new HashSet<>(errorReportDeploy.getErrorReportWithRequestBodyContentTypes());

        RequestBodyReader jsonRequestBodyReader = new JsonRequestBodyReader();
        RequestBodyReader multipartRequestBodyReader = new MultipartRequestBodyReader();

        REQUEST_BODY_READER_HOLDER.put(jsonRequestBodyReader.getContentType(), jsonRequestBodyReader);
        REQUEST_BODY_READER_HOLDER.put(multipartRequestBodyReader.getContentType(), multipartRequestBodyReader);
    }

    private static Set<String> withRequestBodyContentTypes;

    private static final Predicate<String> WITH_REQUEST_BODY_PRE = contentType ->
            withRequestBodyContentTypes.contains(contentType);

    private final Map<String, RequestBodyReader> REQUEST_BODY_READER_HOLDER = new HashMap<>(4, 2.0f);

    private final Function<HttpHeaders, RequestBodyReader> REQUEST_BODY_PROCESSOR_GETTER = headers -> {
        RequestBodyReader processor = REQUEST_BODY_READER_HOLDER.get(HEADER_VALUE_GETTER.apply(headers, HttpHeaders.CONTENT_TYPE));

        if (isNull(processor))
            throw new BlueException(UNSUPPORTED_MEDIA_TYPE);

        return processor;
    };

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

    private final BiConsumer<ServerHttpRequest, Map<String, Object>> ATTR_PACKAGER = (request, attributes) -> {

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
    };

    @SuppressWarnings({"NullableProblems", "DuplicatedCode"})
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        Map<String, Object> attributes = exchange.getAttributes();

        ATTR_PACKAGER.accept(request, attributes);

        return chain.filter(exchange)
                .onErrorResume(throwable -> {
                    DataEvent dataEvent = new DataEvent();
                    dataEvent.setDataEventType(UNIFIED.identity);
                    dataEvent.setDataEventOpType(CLICK.identity);

                    dataEvent.setStamp(TIME_STAMP_GETTER.get());
                    EVENT_ATTR_PACKAGER.accept(attributes, dataEvent);
                    report(throwable, request, dataEvent);

                    if (WITH_REQUEST_BODY_PRE.test(HEADER_VALUE_GETTER.apply(request.getHeaders(), HttpHeaders.CONTENT_TYPE))) {
                        return REQUEST_BODY_PROCESSOR_GETTER.apply(request.getHeaders())
                                .processor(exchange)
                                .flatMap(requestBody -> {
                                    dataEvent.addData(REQUEST_BODY.key, requestBody);
                                    report(throwable, request, dataEvent);
                                    return error(throwable);
                                });
                    } else {
                        report(throwable, request, dataEvent);
                        return error(throwable);
                    }
                });
    }

    @Override
    public int getOrder() {
        return BLUE_PRE_WITH_ERROR_REPORT.order;
    }


    /**
     * request body reader
     *
     * @author liuyunfei
     */
    @SuppressWarnings("JavaDoc")
    protected interface RequestBodyReader {

        /**
         * handle type
         *
         * @return
         */
        String getContentType();

        /**
         * handle
         *
         * @param exchange
         * @return
         */
        Mono<String> processor(ServerWebExchange exchange);

    }

    /**
     * impl for json
     */
    @SuppressWarnings("JavaDoc")
    protected class JsonRequestBodyReader implements RequestBodyReader {

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
         * @param exchange
         * @return
         */
        @Override
        public Mono<String> processor(ServerWebExchange exchange) {
            return ServerRequest.create(exchange, httpMessageReaders)
                    .bodyToMono(String.class)
                    .switchIfEmpty(defer(() -> just(EMPTY_VALUE.value)));
        }
    }

    /**
     * impl for part
     */
    @SuppressWarnings("JavaDoc")
    protected static class MultipartRequestBodyReader implements RequestBodyReader {

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
         * @param exchange
         * @return
         */
        @Override
        public Mono<String> processor(ServerWebExchange exchange) {
            return extractValuesToBind(exchange)
                    .flatMap(params ->
                            just(GSON.toJson(params))
                    );
        }
    }

}

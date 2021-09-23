package com.blue.file.config.filter.global;

import com.blue.base.common.base.CommonFunctions;
import com.blue.base.component.exception.handler.model.ExceptionHandleInfo;
import com.blue.base.constant.base.BlueHeader;
import com.blue.base.model.event.data.DataEvent;
import com.blue.base.model.exps.BlueException;
import com.blue.file.config.common.FluxCommonFactory;
import com.blue.file.config.common.request.body.RequestBodyGetter;
import com.blue.file.config.component.RequestEventReporter;
import com.blue.file.config.deploy.ErrorReportDeploy;
import com.google.gson.Gson;
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

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.function.*;

import static com.blue.base.common.reactive.ReactiveCommonFunctions.getIp;
import static com.blue.base.constant.base.BlueDataAttrKey.*;
import static com.blue.base.constant.base.DataEventType.UNIFIED;
import static com.blue.base.constant.base.ResponseElement.UNSUPPORTED_MEDIA_TYPE;
import static com.blue.file.config.common.FluxCommonFactory.extractValuesToBind;
import static com.blue.file.config.filter.BlueFilterOrder.BLUE_ERROR_REPORT;
import static java.lang.String.valueOf;
import static java.util.Optional.ofNullable;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * 熔断降级过滤器
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "SpringJavaInjectionPointsAutowiringInspection"})
@Component
public final class BlueErrorReportFilter implements WebFilter, Ordered {

    private static final Logger LOGGER = getLogger(BlueErrorReportFilter.class);

    private final ExecutorService executorService;

    private final RequestEventReporter requestEventReporter;

    public BlueErrorReportFilter(ExecutorService executorService, RequestEventReporter requestEventReporter, ErrorReportDeploy errorReportDeploy) {
        this.executorService = executorService;
        this.requestEventReporter = requestEventReporter;
        withRequestBodyContentTypes = new HashSet<>(errorReportDeploy.getErrorReportWithRequestBodyContentTypes());
    }

    /**
     * 入口处断言请求方式,防止后续非法请求过多致使intern影响常量池
     */
    private static final Consumer<String> METHOD_VALUE_ASSERTER = FluxCommonFactory.METHOD_VALUE_ASSERTER;

    private static final String AUTHORIZATION = BlueHeader.AUTHORIZATION.name;

    /**
     * 记录请求体的contentType
     */
    private static Set<String> withRequestBodyContentTypes;

    /**
     * 是否上报请求体
     */
    private static final Predicate<String> WITH_REQUEST_BODY_PRE = contentType ->
            withRequestBodyContentTypes.contains(contentType);

    /**
     * messageReader
     */
    private static final List<HttpMessageReader<?>> MESSAGE_READERS = FluxCommonFactory.MESSAGE_READERS;

    /**
     * 秒级时间戳获取器
     */
    private static final Supplier<Long> TIME_STAMP_GETTER = FluxCommonFactory.TIME_STAMP_GETTER;

    private static final Gson GSON = CommonFunctions.GSON;

    /**
     * 异常转换器
     */
    private static final Function<Throwable, ExceptionHandleInfo> THROWABLE_CONVERTER = FluxCommonFactory.THROWABLE_CONVERTER;

    private static final Supplier<String> RANDOM_KEY_GENERATOR = FluxCommonFactory.RANDOM_KEY_GETTER;

    /**
     * getter holder
     */
    private final Map<String, RequestBodyGetter> REQUEST_BODY_GETTER_HOLDER = new HashMap<>(4, 1.0f);

    /**
     * 获取真实value
     */
    public static final BiFunction<HttpHeaders, String, String> HEADER_VALUE_GETTER = FluxCommonFactory.HEADER_VALUE_GETTER;

    /**
     * 获取请求体解析器
     */
    private final Function<HttpHeaders, RequestBodyGetter> REQUEST_BODY_PROCESSOR_GETTER = headers -> {
        RequestBodyGetter processor = REQUEST_BODY_GETTER_HOLDER.get(HEADER_VALUE_GETTER.apply(headers, HttpHeaders.CONTENT_TYPE));

        if (processor == null)
            throw new BlueException(UNSUPPORTED_MEDIA_TYPE.status, UNSUPPORTED_MEDIA_TYPE.code, UNSUPPORTED_MEDIA_TYPE.message);

        return processor;
    };

    /**
     * 数据上报
     *
     * @param dataEvent
     * @param throwable
     */
    private void report(DataEvent dataEvent, Throwable throwable) {
        try {
            executorService.submit(() -> {
                ExceptionHandleInfo exceptionHandleInfo = THROWABLE_CONVERTER.apply(throwable);

                dataEvent.addData(RESPONSE_STATUS.key, valueOf(exceptionHandleInfo.getCode()).intern());
                dataEvent.addData(RESPONSE_BODY.key, GSON.toJson(exceptionHandleInfo.getBlueVo()));

                requestEventReporter.report(dataEvent);
                LOGGER.info("report exception event, dataEvent = {}", dataEvent);
            });
        } catch (Exception e) {
            LOGGER.error("report failed, dataEvent = {}, throwable = {}, e = {}",
                    dataEvent, throwable, e);
        }
    }

    /**
     * 初始化
     */
    @PostConstruct
    private void init() {
        RequestBodyGetter jsonRequestBodyGetter = new JsonRequestBodyGetter();
        RequestBodyGetter multipartRequestBodyGetter = new MultipartRequestBodyGetter();

        REQUEST_BODY_GETTER_HOLDER.put(jsonRequestBodyGetter.getContentType(), jsonRequestBodyGetter);
        REQUEST_BODY_GETTER_HOLDER.put(multipartRequestBodyGetter.getContentType(), multipartRequestBodyGetter);
    }

    @SuppressWarnings({"NullableProblems", "DuplicatedCode"})
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        String methodValue = request.getMethodValue();
        METHOD_VALUE_ASSERTER.accept(methodValue);

        String requestId = RANDOM_KEY_GENERATOR.get();
        String clientIp = getIp(request);

        LOGGER.info("blueErrorReportFilter -> requestId = {}, clientIp = {}", requestId, clientIp);

        Map<String, Object> attributes = exchange.getAttributes();

        attributes.put(REQUEST_ID.key, requestId);
        attributes.put(CLIENT_IP.key, clientIp);

        ofNullable(request.getHeaders().getFirst(BlueHeader.METADATA.name))
                .ifPresent(metadata -> attributes.put(METADATA.key, metadata));
        ofNullable(request.getHeaders().getFirst(AUTHORIZATION))
                .ifPresent(jwt -> attributes.put(JWT.key, jwt));

        return chain.filter(exchange)
                .onErrorResume(throwable -> {
                    DataEvent dataEvent = new DataEvent();

                    dataEvent.setDataEventType(UNIFIED);
                    dataEvent.setStamp(TIME_STAMP_GETTER.get());

                    dataEvent.addData(REQUEST_ID.key, requestId);
                    dataEvent.addData(CLIENT_IP.key, clientIp);

                    ofNullable(attributes.get(METADATA.key)).map(String::valueOf)
                            .ifPresent(metadata -> dataEvent.addData(METADATA.key, metadata));
                    ofNullable(attributes.get(JWT.key)).map(String::valueOf)
                            .ifPresent(jwt -> dataEvent.addData(JWT.key, jwt));
                    ofNullable(attributes.get(ACCESS.key)).map(String::valueOf)
                            .ifPresent(access -> dataEvent.addData(ACCESS.key, access));

                    dataEvent.addData(METHOD.key, methodValue.intern());
                    dataEvent.addData(URI.key, request.getURI().getRawPath().intern());

                    if (WITH_REQUEST_BODY_PRE.test(HEADER_VALUE_GETTER.apply(request.getHeaders(), HttpHeaders.CONTENT_TYPE))) {
                        return REQUEST_BODY_PROCESSOR_GETTER.apply(request.getHeaders())
                                .processor(exchange)
                                .flatMap(requestBody -> {
                                    dataEvent.addData(REQUEST_BODY.key, requestBody);
                                    report(dataEvent, throwable);
                                    return error(throwable);
                                });
                    } else {
                        report(dataEvent, throwable);
                        return error(throwable);
                    }
                });
    }

    @Override
    public int getOrder() {
        return BLUE_ERROR_REPORT.order;
    }


    /**
     * 获取json格式请求体
     */
    protected static class JsonRequestBodyGetter implements RequestBodyGetter {
        @Override
        public String getContentType() {
            return APPLICATION_JSON_VALUE;
        }

        @Override
        public Mono<String> processor(ServerWebExchange exchange) {
            return ServerRequest.create(exchange, MESSAGE_READERS)
                    .bodyToMono(String.class)
                    .switchIfEmpty(
                            just(""));
        }

    }

    /**
     * 处理文件请求
     */
    protected static class MultipartRequestBodyGetter implements RequestBodyGetter {
        @Override
        public String getContentType() {
            return MULTIPART_FORM_DATA_VALUE;
        }

        @Override
        public Mono<String> processor(ServerWebExchange exchange) {
            return extractValuesToBind(exchange)
                    .flatMap(params ->
                            just(GSON.toJson(params))
                    );
        }
    }

}

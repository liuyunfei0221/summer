package com.blue.media.common;

import com.blue.basic.common.reactive.ReactiveCommonFunctions;
import com.blue.basic.model.exps.BlueException;
import com.blue.media.common.part.PartInfoProcessor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.codec.multipart.Part;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.basic.constant.common.ResponseElement.INVALID_PARAM;
import static io.netty.buffer.ByteBufAllocator.DEFAULT;
import static java.lang.String.valueOf;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.*;
import static org.springframework.core.io.buffer.DataBufferUtils.release;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * common factory for webflux
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces"})
public final class MediaCommonFunctions extends ReactiveCommonFunctions {

    private static final Logger LOGGER = getLogger(MediaCommonFunctions.class);

    public static final DataBufferFactory DATA_BUFFER_FACTORY = new NettyDataBufferFactory(DEFAULT);

    public static final int BUFFER_SIZE = 8192;

    /**
     * buffer allocate size
     */
    public static final int BUFFER_ALLOCATE = 512;

    /**
     * error message consumer
     */
    public static final BiConsumer<Throwable, ServerHttpRequestDecorator> ON_ERROR_CONSUMER = (throwable, decorator) -> {
        decorator.getBody()
                .flatMap(dataBuffer -> {
                    if (dataBuffer.readableByteCount() > 0) {
                        release(dataBuffer);
                    }
                    return empty();
                }).doFinally(signalType -> {
                    LOGGER.info("signalType = {}", signalType.toString());
                    LOGGER.error("throwable = {}", throwable);
                }).subscribe();

        LOGGER.error("throwable = {}", throwable);
        if (throwable instanceof BlueException)
            throw (BlueException) throwable;

        throw new RuntimeException(throwable);
    };

    public static final Function<byte[], Flux<DataBuffer>> BUFFER_FLUX_CONVERTER = bytes -> {
        if (isNull(bytes))
            throw new BlueException(EMPTY_PARAM);

        final byte[] tarBytes = bytes;
        int len = tarBytes.length;

        return Flux.create(fluxSink -> {
                    int limit = 0;
                    int rows = BUFFER_ALLOCATE;
                    int last;

                    while (limit < len) {
                        if ((last = len - limit) < rows)
                            rows = last;

                        DataBuffer dataBuffer = DATA_BUFFER_FACTORY.allocateBuffer(rows);
                        dataBuffer.write(tarBytes, limit, rows);
                        fluxSink.next(dataBuffer);

                        limit += rows;
                    }

                    fluxSink.complete();
                }
        );
    };

    /**
     * decorator generator
     */
    public static final BiFunction<ServerHttpRequest, Mono<String>, ServerHttpRequestDecorator> REQUEST_DECORATOR_GENERATOR =
            (request, dataMono) ->
                    new ServerHttpRequestDecorator(request) {
                        @SuppressWarnings("NullableProblems")
                        @Override
                        public Flux<DataBuffer> getBody() {
                            //noinspection CallingSubscribeInNonBlockingScope
                            return Flux.create(fluxSink ->
                                    dataMono
                                            .onErrorStop()
                                            .subscribe(s -> {
                                                        byte[] bytes = s.getBytes(UTF_8);
                                                        int len = bytes.length;

                                                        int limit = 0;
                                                        int rows = BUFFER_ALLOCATE;
                                                        int last;

                                                        while (limit < len) {
                                                            if ((last = len - limit) < rows)
                                                                rows = last;

                                                            DataBuffer dataBuffer = DATA_BUFFER_FACTORY.allocateBuffer(rows);
                                                            dataBuffer.write(bytes, limit, rows);
                                                            fluxSink.next(dataBuffer);

                                                            limit += rows;
                                                        }

                                                        //noinspection UnusedAssignment
                                                        bytes = null;
                                                        fluxSink.complete();
                                                    }
                                            )
                            );
                        }
                    };

    public static final UnaryOperator<String> FILE_TYPE_GETTER = fileName -> {
        if (isBlank(fileName))
            throw new BlueException(INVALID_PARAM);

        int lastIndex = lastIndexOf(fileName, SCHEME_SEPARATOR);
        if (lastIndex < 0 || lastIndex == fileName.length() - 1)
            throw new BlueException(INVALID_PARAM);

        return substring(fileName, lastIndex + 1);
    };

    private static void addBindStringValue(Map<String, String> params, String key, List<String> values) {
        ofNullable(values)
                .filter(vs -> vs.size() > 0)
                .ifPresent(vs -> params.put(key, vs.size() == 1 ? valueOf(vs.get(0)) : vs.toString()));
    }

    private static void addBindPartValue(Map<String, String> params, String key, List<Part> values) {
        ofNullable(values)
                .filter(vs -> vs.size() > 0)
                .map(vs ->
                        values
                                .stream()
                                .map(PartInfoProcessor::process)
                                .map(GSON::toJson)
                                .collect(toList())
                ).ifPresent(vs ->
                        params.put(key, vs.size() == 1 ? vs.get(0) : vs.toString())
                );
    }

    public static Mono<Map<String, String>> extractValuesToBind(ServerWebExchange exchange) {
        MultiValueMap<String, String> queryParams = exchange.getRequest().getQueryParams();
        Mono<MultiValueMap<String, String>> formData = exchange.getFormData();
        Mono<MultiValueMap<String, Part>> multipartData = exchange.getMultipartData();

        return zip(just(queryParams), formData, multipartData)
                .map(tuple -> {
                    Map<String, String> result = new HashMap<>(4, 1.0f);
                    tuple.getT1().forEach((key, values) -> addBindStringValue(result, key, values));
                    tuple.getT2().forEach((key, values) -> addBindStringValue(result, key, values));
                    tuple.getT3().forEach((key, values) -> addBindPartValue(result, key, values));
                    return result;
                });
    }

}

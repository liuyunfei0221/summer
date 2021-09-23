package com.blue.file.config.common;

import com.blue.base.common.reactive.ReactiveCommonFunctions;
import com.blue.base.model.exps.BlueException;
import com.blue.file.config.common.request.part.common.PartInfoProcessor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.multipart.Part;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static io.netty.buffer.ByteBufAllocator.DEFAULT;
import static java.lang.String.valueOf;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.springframework.core.io.buffer.DataBufferUtils.release;
import static org.springframework.web.reactive.function.server.HandlerStrategies.withDefaults;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * 用于webflux的通用组件工厂类
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces"})
public final class FluxCommonFactory extends ReactiveCommonFunctions {

    private static final Logger LOGGER = getLogger(FluxCommonFactory.class);

    public static final List<HttpMessageReader<?>> MESSAGE_READERS = withDefaults().messageReaders();

    public static final DataBufferFactory DATA_BUFFER_FACTORY = new NettyDataBufferFactory(DEFAULT);

    /**
     * ALLOCATE大小
     */
    private static final int BUFFER_ALLOCATE = 512;

    /**
     * 错误处理
     */
    public static final BiConsumer<Throwable, ServerHttpRequestDecorator> ON_ERROR_CONSUMER = (throwable, decorator) -> {
        decorator.getBody()
                .flatMap(dataBuffer -> {
                    if (dataBuffer.readableByteCount() > 0) {
                        release(dataBuffer);
                    }
                    return empty();
                }).doFinally(signalType ->
                        error(throwable));

        LOGGER.info("throwable = {0}", throwable.getCause());

        if (throwable instanceof RuntimeException)
            throw (RuntimeException) throwable;

        throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, INTERNAL_SERVER_ERROR.message);
    };

    /**
     * 封装request
     */
    public static final BiFunction<ServerHttpRequest, Mono<String>, ServerHttpRequestDecorator> REQUEST_DECORATOR_GENERATOR =
            (request, dataMono) -> {
                //noinspection NullableProblems
                return new ServerHttpRequestDecorator(request) {
                    @Override
                    public Flux<DataBuffer> getBody() {
                        return dataMono.flatMapIterable(s -> {
                            byte[] bytes = s.getBytes(UTF_8);
                            int len = bytes.length;

                            int limit = 0;
                            int rows;
                            int last;

                            LinkedList<DataBuffer> buffers = new LinkedList<>();

                            while (limit < len) {
                                rows = BUFFER_ALLOCATE;
                                if ((last = len - limit) < rows)
                                    rows = last;

                                DataBuffer dataBuffer = DATA_BUFFER_FACTORY.allocateBuffer(rows);
                                dataBuffer.write(bytes, limit, rows);
                                buffers.add(dataBuffer);

                                limit += rows;
                            }

                            //noinspection UnusedAssignment
                            bytes = null;

                            return buffers;
                        });
                    }
                };
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

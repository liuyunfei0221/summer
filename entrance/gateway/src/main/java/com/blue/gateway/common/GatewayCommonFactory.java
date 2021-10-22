package com.blue.gateway.common;

import com.blue.base.common.reactive.ReactiveCommonFunctions;
import com.blue.base.model.exps.BlueException;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import reactor.core.publisher.Flux;
import reactor.util.Logger;

import java.util.List;
import java.util.function.BiConsumer;

import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static org.springframework.core.io.buffer.DataBufferUtils.release;
import static org.springframework.web.reactive.function.server.HandlerStrategies.withDefaults;
import static reactor.core.publisher.Mono.empty;
import static reactor.core.publisher.Mono.error;
import static reactor.util.Loggers.getLogger;

/**
 * common factory for gateway
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "JavaDoc", "AliControlFlowStatementWithoutBraces"})
public final class GatewayCommonFactory extends ReactiveCommonFunctions {

    private static final Logger LOGGER = getLogger(GatewayCommonFactory.class);

    public static final List<HttpMessageReader<?>> MESSAGE_READERS = withDefaults().messageReaders();

    /**
     * error message consumer
     */
    public static final BiConsumer<Throwable, CachedBodyOutputMessage> ON_ERROR_CONSUMER_WITH_MESSAGE = (throwable, outputMessage) -> {
        outputMessage.getBody()
                .flatMap(dataBuffer -> {
                    if (dataBuffer.readableByteCount() > 0)
                        release(dataBuffer);
                    return empty();
                })
                .doFinally(signalType -> {
                    LOGGER.info("signalType = {}", signalType.toString());
                    error(throwable);
                });

        LOGGER.info("throwable = {}", throwable);
        if (throwable instanceof RuntimeException)
            throw (RuntimeException) throwable;

        throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, INTERNAL_SERVER_ERROR.message);
    };

    /**
     * generate a new ServerHttpRequestDecorator
     *
     * @param request
     * @param headers
     * @param outputMessage
     * @return
     */
    public static ServerHttpRequestDecorator getRequestDecorator(ServerHttpRequest request, HttpHeaders headers,
                                                                 CachedBodyOutputMessage outputMessage) {
        //noinspection NullableProblems
        return new ServerHttpRequestDecorator(request) {
            @Override
            public HttpHeaders getHeaders() {
                return headers;
            }

            @Override
            public Flux<DataBuffer> getBody() {
                return outputMessage.getBody();
            }
        };
    }

}

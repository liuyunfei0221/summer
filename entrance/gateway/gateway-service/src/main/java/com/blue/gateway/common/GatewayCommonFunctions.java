package com.blue.gateway.common;

import com.blue.basic.common.base.CommonFunctions;
import com.blue.basic.model.exps.BlueException;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import reactor.core.publisher.Flux;
import reactor.util.Logger;

import java.util.function.BiConsumer;

import static org.springframework.core.io.buffer.DataBufferUtils.release;
import static reactor.core.publisher.Mono.empty;
import static reactor.util.Loggers.getLogger;

/**
 * common factory for gateway
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "JavaDoc", "AliControlFlowStatementWithoutBraces"})
public final class GatewayCommonFunctions extends CommonFunctions {

    private static final Logger LOGGER = getLogger(GatewayCommonFunctions.class);

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
                    LOGGER.error("throwable = {}", throwable);
                }).subscribe();

        LOGGER.info("throwable = {}", throwable);
        if (throwable instanceof BlueException)
            throw (BlueException) throwable;

        throw new RuntimeException(throwable);
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

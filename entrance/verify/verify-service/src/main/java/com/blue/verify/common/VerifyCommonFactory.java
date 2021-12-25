package com.blue.verify.common;

import com.blue.base.common.reactive.ReactiveCommonFunctions;
import com.blue.base.model.exps.BlueException;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static io.netty.buffer.ByteBufAllocator.DEFAULT;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.core.io.buffer.DataBufferUtils.release;
import static org.springframework.web.reactive.function.server.HandlerStrategies.withDefaults;
import static reactor.core.publisher.Mono.empty;
import static reactor.core.publisher.Mono.error;
import static reactor.util.Loggers.getLogger;

/**
 * common factory for webflux
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces"})
public final class VerifyCommonFactory extends ReactiveCommonFunctions {

    private static final Logger LOGGER = getLogger(VerifyCommonFactory.class);

    public static final List<HttpMessageReader<?>> MESSAGE_READERS = withDefaults().messageReaders();

    public static final DataBufferFactory DATA_BUFFER_FACTORY = new NettyDataBufferFactory(DEFAULT);

    /**
     * buffer allocate size
     */
    private static final int BUFFER_ALLOCATE = 512;

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
                }).doFinally(signalType ->
                        error(throwable));

        LOGGER.info("throwable = {}", throwable);
        if (throwable instanceof BlueException)
            throw (BlueException) throwable;

        throw (RuntimeException) throwable;
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

}

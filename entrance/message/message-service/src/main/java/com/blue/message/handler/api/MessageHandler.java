package com.blue.message.handler.api;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.util.DefaultPayload;
import org.reactivestreams.Publisher;
import org.springframework.lang.NonNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * message api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "BlockingMethodInNonBlockingContext"})
public final class MessageHandler implements RSocket {

    private static final Logger LOGGER = getLogger(MessageHandler.class);

    @Override
    @NonNull
    public Mono<Void> fireAndForget(@NonNull Payload payload) {
        String data = payload.getDataUtf8();
        LOGGER.warn("fireAndForget = {}", data);

        return RSocket.super.fireAndForget(payload);
    }

    @Override
    @NonNull
    public Mono<Payload> requestResponse(@NonNull Payload payload) {
        String data = payload.getDataUtf8();
        LOGGER.warn("requestResponse = {}", data);

        return just(DefaultPayload.create("requestResponse = " + data));
    }

    @Override
    @NonNull
    public Flux<Payload> requestStream(@NonNull Payload payload) {
        String data = payload.getDataUtf8();
        LOGGER.warn("requestStream = {}", data);

        List<String> resData = IntStream.range(0, 100)
                .mapToObj(i -> data + " " + i)
                .collect(toList());

        return Flux.create(fluxSink -> {
            try {
                for (String res : resData) {
                    fluxSink.next(res);

                    TimeUnit.SECONDS.sleep(2);
                }
            } catch (Exception e) {
                LOGGER.error("e = {}", e);
            }

            fluxSink.complete();
        }).cast(String.class).map(DefaultPayload::create);
    }

    @Override
    @NonNull
    public Flux<Payload> requestChannel(@NonNull Publisher<Payload> payloads) {
        AtomicLong atomicLong = new AtomicLong();

        return Flux.from(payloads)
                .map(payload -> {
                    String data = payload.getDataUtf8();
                    LOGGER.warn("requestChannel = {}", data);

                    return DefaultPayload.create(data + " " + atomicLong.getAndIncrement());
                });
    }

    @Override
    @NonNull
    public Mono<Void> metadataPush(@NonNull Payload payload) {
        return RSocket.super.metadataPush(payload);
    }

    @Override
    public double availability() {
        return RSocket.super.availability();
    }

    @Override
    public void dispose() {
        RSocket.super.dispose();
    }

    @Override
    public boolean isDisposed() {
        return RSocket.super.isDisposed();
    }

    @Override
    @NonNull
    public Mono<Void> onClose() {
        return RSocket.super.onClose();
    }

}

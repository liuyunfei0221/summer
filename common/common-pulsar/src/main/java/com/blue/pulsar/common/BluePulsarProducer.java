package com.blue.pulsar.common;

import com.blue.pulsar.api.conf.ProducerConf;
import org.apache.pulsar.client.api.*;
import org.apache.pulsar.client.api.interceptor.ProducerInterceptor;
import org.apache.pulsar.client.api.transaction.Transaction;
import org.slf4j.Logger;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.blue.pulsar.utils.PulsarCommonsGenerator.generateClient;
import static com.blue.pulsar.utils.PulsarCommonsGenerator.generateProducer;
import static java.lang.System.currentTimeMillis;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * pulsar producer
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "unused"})
public final class BluePulsarProducer<T extends Serializable> {

    private static final Logger LOGGER = getLogger(BluePulsarProducer.class);

    private final PulsarClient pulsarClient;

    private final Producer<T> pulsarProducer;

    public BluePulsarProducer(ProducerConf conf, Class<T> clz, MessageRouter messageRouter,
                              BatcherBuilder batcherBuilder, List<ProducerInterceptor> interceptors) {
        this.pulsarClient = generateClient(conf);
        this.pulsarProducer = generateProducer(pulsarClient, conf, clz, messageRouter, batcherBuilder, interceptors);
    }

    String getTopic() {
        return pulsarProducer.getTopic();
    }

    String getProducerName() {
        return pulsarProducer.getProducerName();
    }

    /**
     * send
     *
     * @param data
     */
    public MessageId send(T data) {
        LOGGER.info("producer send, data = {}", data);
        if (data != null)
            try {
                return pulsarProducer.send(data);
            } catch (PulsarClientException e) {
                LOGGER.error("send failed, data = {}, e = {}", data, e);
                throw new RuntimeException("producer send failed, cause e = {}", e);
            }

        throw new RuntimeException("data can't be null");
    }

    /**
     * send async
     *
     * @param data
     */
    public CompletableFuture<MessageId> sendAsync(T data) {
        LOGGER.info("producer sendAsync, data = {}", data);
        if (data != null)
            return pulsarProducer.sendAsync(data);

        throw new RuntimeException("data can't be null");
    }

    /**
     * delay send
     *
     * @param data
     * @param delay
     * @param unit
     * @return
     */
    public MessageId sendDeliverAfter(T data, Long delay, TimeUnit unit) {
        LOGGER.info("producer sendDeliverAfter, data = {}, delay = {}, unit = {}", data, delay, unit);
        if (data != null && delay != null && delay > 0L && unit != null)
            try {
                return pulsarProducer.newMessage().value(data).deliverAfter(delay, unit).send();
            } catch (PulsarClientException e) {
                LOGGER.error("send failed, data = {}, delay = {}, unit = {} , e = {}", data, delay, unit, e);
                throw new RuntimeException("producer sendDeliverAfter failed, cause e = {}", e);
            }

        throw new RuntimeException("data or unit can't be null, delay can't be null or less than 1");
    }

    /**
     * delay send async
     *
     * @param data
     * @param delay
     * @param unit
     * @return
     */
    public CompletableFuture<MessageId> sendDeliverAfterAsync(T data, Long delay, TimeUnit unit) {
        LOGGER.info("producer sendDeliverAfterAsync, data = {}, delay = {}, unit = {}", data, delay, unit);
        if (data != null && delay != null && delay > 0L && unit != null)
            return pulsarProducer.newMessage().value(data).deliverAfter(delay, unit).sendAsync();

        throw new RuntimeException("data or unit can't be null, delay can't be null or less than 1");
    }

    /**
     * deliver at send
     *
     * @param data
     * @param timestamp
     * @return
     */
    public MessageId sendDeliverAt(T data, Long timestamp) {
        LOGGER.info("producer sendDeliverAt, data = {}, timestamp = {}", data, timestamp);
        if (data != null && timestamp != null && timestamp > currentTimeMillis())
            try {
                return pulsarProducer.newMessage().value(data).deliverAt(timestamp).send();
            } catch (PulsarClientException e) {
                LOGGER.error("send failed, data = {}, timestamp = {}, e = {}", data, timestamp, e);
                throw new RuntimeException("producer sendDeliverAt failed, cause e = {}", e);
            }

        throw new RuntimeException("data can't be null, timestamp can't be null or less than currentTimeMillis()");
    }

    /**
     * deliver at send async
     *
     * @param data
     * @param timestamp
     * @return
     */
    public CompletableFuture<MessageId> sendDeliverAtAsync(T data, Long timestamp) {
        LOGGER.info("producer sendDeliverAtAsync, data = {}, timestamp = {}", data, timestamp);
        if (data != null && timestamp != null && timestamp > currentTimeMillis())
            return pulsarProducer.newMessage().value(data).deliverAt(timestamp).sendAsync();

        throw new RuntimeException("data can't be null, timestamp can't be null or less than currentTimeMillis()");
    }


    public void flush() throws PulsarClientException {
        pulsarProducer.flush();
    }

    public CompletableFuture<Void> flushAsync() {
        return pulsarProducer.flushAsync();
    }

    public TypedMessageBuilder<T> newMessage() {
        return pulsarProducer.newMessage();
    }

    public <V> TypedMessageBuilder<V> newMessage(Schema<V> schema) {
        return pulsarProducer.newMessage(schema);
    }

    public TypedMessageBuilder<T> newMessage(Transaction txn) {
        return pulsarProducer.newMessage(txn);
    }

    public long getLastSequenceId() {
        return pulsarProducer.getLastSequenceId();
    }

    public ProducerStats getStats() {
        return pulsarProducer.getStats();
    }

    public void close() throws PulsarClientException {
        pulsarProducer.close();
    }

    public CompletableFuture<Void> closeAsync() {
        return pulsarProducer.closeAsync();
    }

    public boolean isConnected() {
        return pulsarProducer.isConnected();
    }

    public long getLastDisconnectedTimestamp() {
        return pulsarProducer.getLastDisconnectedTimestamp();
    }

    /**
     * stop
     */
    public void shutdown() {
        try {
            pulsarProducer.close();
            pulsarClient.close();
            LOGGER.warn("producer shutdown ...");
        } catch (PulsarClientException e) {
            LOGGER.error("producer shutdown failed, cause e = {0}", e);
            throw new RuntimeException("producer shutdown failed, cause e = {}", e);
        }
    }

    /**
     * stop async
     */
    public CompletableFuture<Void> shutdownAsync() {
        return pulsarProducer.closeAsync()
                .thenApplyAsync(v -> pulsarClient.closeAsync())
                .thenAcceptAsync(v ->
                        LOGGER.warn("producer shutdownAsync ...")
                )
                .exceptionally(e -> {
                    LOGGER.error("producer shutdownAsync failed, cause e = {0}", e);
                    return null;
                });
    }

}

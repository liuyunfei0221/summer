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

import static com.blue.pulsar.utils.PulsarCommonsGenerator.generateProducer;
import static java.lang.System.currentTimeMillis;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * pulsar transaction producer
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "unused"})
public final class BlueTransPulsarProducer<T extends Serializable> {

    private static final Logger LOGGER = getLogger(BlueTransPulsarProducer.class);

    private final PulsarClient pulsarClient;

    private final Producer<T> pulsarProducer;

    public BlueTransPulsarProducer(ProducerConf conf, PulsarClient pulsarClient, Class<T> clz, MessageRouter messageRouter,
                                   BatcherBuilder batcherBuilder, List<ProducerInterceptor> interceptors) {
        if (pulsarClient == null)
            throw new RuntimeException("pulsarClient can't be null");

        this.pulsarClient = pulsarClient;
        this.pulsarProducer = generateProducer(pulsarClient, conf, clz, messageRouter, batcherBuilder, interceptors);
    }

    String getTopic() {
        return pulsarProducer.getTopic();
    }

    String getProducerName() {
        return pulsarProducer.getProducerName();
    }

    /**
     * send data with trans
     *
     * @param data
     * @param transaction
     * @return
     */
    public MessageId sendWithTrans(T data, Transaction transaction) {
        LOGGER.info("producer sendWithTrans, data = {}, transaction = {}", data, transaction);
        if (data != null && transaction != null)
            try {
                return pulsarProducer.newMessage(transaction).value(data).send();
            } catch (PulsarClientException e) {
                LOGGER.error("producer sendWithTrans failed, data = {}, transaction = {}, e = {}", data, transaction, e);
                throw new RuntimeException("producer sendWithTrans failed, cause e = " + e);
            }

        throw new RuntimeException("data or params can't be null");
    }

    /**
     * async send data with trans
     *
     * @param data
     * @param transaction
     * @return
     */
    public CompletableFuture<MessageId> sendWithTransAsync(T data, Transaction transaction) {
        LOGGER.info("producer sendWithTransAsync, data = {}, transaction = {}", data, transaction);
        if (data != null && transaction != null)
            return pulsarProducer.newMessage(transaction)
                    .value(data).sendAsync();

        throw new RuntimeException("data or params can't be null");
    }

    /**
     * deliver after send data with trans
     *
     * @param data
     * @param transaction
     * @param delay
     * @param unit
     * @return
     */
    public MessageId sendWithTransDeliverAfter(T data, Transaction transaction, Long delay, TimeUnit unit) {
        LOGGER.info("producer sendWithTransDeliverAfter, data = {}, transaction = {}, delay = {}, unit = {}", data, transaction, delay, unit);
        if (data != null && transaction != null && delay != null && delay > 0L && unit != null)
            try {
                return pulsarProducer.newMessage(transaction).value(data).deliverAfter(delay, unit).send();
            } catch (PulsarClientException e) {
                LOGGER.error("producer sendWithTransDeliverAfter failed, data = {}, transaction = {}, delay = {}, unit = {}, e = {}", data, transaction, delay, unit, e);
                throw new RuntimeException("producer sendWithTransDeliverAfter failed, cause e = {}", e);
            }

        throw new RuntimeException("data or params can't be null");
    }

    /**
     * async deliver after send data with trans
     *
     * @param data
     * @param transaction
     * @param delay
     * @param unit
     * @return
     */
    public CompletableFuture<MessageId> sendWithTransDeliverAfterAsync(T data, Transaction transaction, Long delay, TimeUnit unit) {
        LOGGER.info("producer sendWithTransDeliverAfterAsync, data = {}, transaction = {}, delay = {}, unit = {}", data, transaction, delay, unit);
        if (data != null && transaction != null && delay != null && delay > 0L && unit != null)
            return pulsarProducer.newMessage(transaction).value(data).deliverAfter(delay, unit).sendAsync();

        throw new RuntimeException("data or params can't be null");
    }

    /**
     * deliver at send data with trans
     *
     * @param data
     * @param transaction
     * @param timestamp
     * @return
     */
    public MessageId sendWithTransDeliverAt(T data, Transaction transaction, Long timestamp) {
        LOGGER.info("producer sendWithTransDeliverAt, data = {}, transaction = {}, timestamp = {}", data, transaction, timestamp);
        if (data != null && transaction != null && timestamp != null && timestamp > currentTimeMillis())
            try {
                return pulsarProducer.newMessage(transaction).value(data).deliverAt(timestamp).send();
            } catch (PulsarClientException e) {
                LOGGER.error("producer sendWithTransDeliverAt failed, data = {}, transaction = {}, timestamp = {}, e = {}", data, transaction, timestamp, e);
                throw new RuntimeException("producer sendWithTransDeliverAt failed, cause e = {}", e);
            }

        throw new RuntimeException("data or params can't be null");
    }

    /**
     * async deliver at send data with trans
     *
     * @param data
     * @param transaction
     * @param timestamp
     * @return
     */
    public CompletableFuture<MessageId> sendWithTransDeliverAtAsync(T data, Transaction transaction, Long timestamp) {
        LOGGER.info("producer sendWithTransDeliverAtAsync, data = {}, transaction = {}, timestamp = {}", data, transaction, timestamp);
        if (data != null && transaction != null && timestamp != null && timestamp > currentTimeMillis())
            return pulsarProducer.newMessage(transaction).value(data).deliverAt(timestamp).sendAsync();

        throw new RuntimeException("data or params can't be null");
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
                    throw new RuntimeException("producer shutdownAsync failed, cause e = " + e);
                });
    }

}

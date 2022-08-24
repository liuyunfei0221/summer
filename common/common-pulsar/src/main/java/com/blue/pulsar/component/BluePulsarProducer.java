package com.blue.pulsar.component;

import com.blue.pulsar.api.conf.ProducerConf;
import org.apache.pulsar.client.api.*;
import org.apache.pulsar.client.api.interceptor.ProducerInterceptor;
import org.apache.pulsar.client.api.transaction.Transaction;
import org.slf4j.Logger;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.blue.pulsar.common.PulsarCommonsGenerator.generateProducer;
import static java.lang.System.currentTimeMillis;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * pulsar producer
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "unused"})
public final class BluePulsarProducer<T extends Serializable> implements Producer<T> {

    private static final Logger LOGGER = getLogger(BluePulsarProducer.class);

    private final Producer<T> pulsarProducer;

    public BluePulsarProducer(PulsarClient pulsarClient, ProducerConf conf, Class<T> clz, MessageRouter messageRouter,
                              BatcherBuilder batcherBuilder, List<ProducerInterceptor> interceptors) {
        this.pulsarProducer = generateProducer(pulsarClient, conf, clz, messageRouter, batcherBuilder, interceptors);
    }

    @Override
    public String getTopic() {
        return pulsarProducer.getTopic();
    }

    @Override
    public String getProducerName() {
        return pulsarProducer.getProducerName();
    }

    @Override
    public MessageId send(T message) {
        LOGGER.info("producer send, message = {}", message);
        if (message != null)
            try {
                return pulsarProducer.send(message);
            } catch (PulsarClientException e) {
                LOGGER.error("send failed, message = {}, e = {}", message, e);
                throw new RuntimeException("producer send failed");
            }

        throw new RuntimeException("message can't be null");
    }

    @Override
    public CompletableFuture<MessageId> sendAsync(T message) {
        LOGGER.info("producer sendAsync, message = {}", message);
        if (message != null)
            return pulsarProducer.sendAsync(message);

        throw new RuntimeException("message can't be null");
    }

    public MessageId sendDeliverAfter(T message, Long delay, TimeUnit unit) {
        LOGGER.info("producer sendDeliverAfter, message = {}, delay = {}, unit = {}", message, delay, unit);
        if (message != null && delay != null && delay > 0L && unit != null)
            try {
                return pulsarProducer.newMessage().value(message).deliverAfter(delay, unit).send();
            } catch (PulsarClientException e) {
                LOGGER.error("send failed, message = {}, delay = {}, unit = {} , e = {}", message, delay, unit, e);
                throw new RuntimeException("producer send failed");
            }

        throw new RuntimeException("message can't be null");
    }

    public CompletableFuture<MessageId> sendDeliverAfterAsync(T message, Long delay, TimeUnit unit) {
        LOGGER.info("producer sendDeliverAfterAsync, message = {}, delay = {}, unit = {}", message, delay, unit);
        if (message != null && delay != null && delay > 0L && unit != null)
            return pulsarProducer.newMessage().value(message).deliverAfter(delay, unit).sendAsync();

        throw new RuntimeException("message can't be null");
    }

    public MessageId sendDeliverAt(T message, Long timestamp) {
        LOGGER.info("producer sendDeliverAt, message = {}, timestamp = {}", message, timestamp);
        if (message != null && timestamp != null && timestamp > currentTimeMillis())
            try {
                return pulsarProducer.newMessage().value(message).deliverAt(timestamp).send();
            } catch (PulsarClientException e) {
                LOGGER.error("send failed, message = {}, timestamp = {}, e = {}", message, timestamp, e);
                throw new RuntimeException("producer send failed");
            }

        throw new RuntimeException("message can't be null");
    }

    public CompletableFuture<MessageId> sendDeliverAtAsync(T message, Long timestamp) {
        LOGGER.info("producer sendDeliverAtAsync, message = {}, timestamp = {}", message, timestamp);
        if (message != null && timestamp != null && timestamp > currentTimeMillis())
            return pulsarProducer.newMessage().value(message).deliverAt(timestamp).sendAsync();

        throw new RuntimeException("message can't be null");
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

    @Override
    public void flush() {
        try {
            pulsarProducer.flush();
        } catch (PulsarClientException e) {
            throw new RuntimeException("flush failed, e = " + e);
        }
    }

    @Override
    public CompletableFuture<Void> flushAsync() {
        return pulsarProducer.flushAsync();
    }

    @Override
    public TypedMessageBuilder<T> newMessage() {
        return pulsarProducer.newMessage();
    }

    @Override
    public <V> TypedMessageBuilder<V> newMessage(Schema<V> schema) {
        return pulsarProducer.newMessage(schema);
    }

    @Override
    public TypedMessageBuilder<T> newMessage(Transaction txn) {
        return pulsarProducer.newMessage(txn);
    }

    @Override
    public long getLastSequenceId() {
        return pulsarProducer.getLastSequenceId();
    }

    @Override
    public ProducerStats getStats() {
        return pulsarProducer.getStats();
    }

    @Override
    public void close() {
        try {
            pulsarProducer.close();
        } catch (PulsarClientException e) {
            throw new RuntimeException("close failed, e = " + e);
        }
    }

    @Override
    public CompletableFuture<Void> closeAsync() {
        return pulsarProducer.closeAsync();
    }

    @Override
    public boolean isConnected() {
        return pulsarProducer.isConnected();
    }

    @Override
    public long getLastDisconnectedTimestamp() {
        return pulsarProducer.getLastDisconnectedTimestamp();
    }

    @Override
    public int getNumOfPartitions() {
        return pulsarProducer.getNumOfPartitions();
    }

}
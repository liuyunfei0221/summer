package com.blue.pulsar.common;

import com.blue.pulsar.api.conf.ConsumerConf;
import org.apache.pulsar.client.api.*;
import org.apache.pulsar.client.api.transaction.Transaction;
import org.slf4j.Logger;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static com.blue.pulsar.utils.PulsarCommonsGenerator.generateConsumer;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * pulsar transaction consumer
 *
 * @author liuyunfei
 */
@SuppressWarnings({"FieldCanBeLocal", "unused", "AliControlFlowStatementWithoutBraces"})
public final class BlueTransPulsarConsumer<T extends Serializable> {

    private static final Logger LOGGER = getLogger(BlueTransPulsarConsumer.class);

    private final PulsarClient client;
    private final Consumer<T> consumer;

    public BlueTransPulsarConsumer(ConsumerConf conf, PulsarClient pulsarClient, Class<T> clz, MessageListener<T> messageListener, ConsumerEventListener consumerEventListener,
                                   List<ConsumerInterceptor<T>> interceptors, KeySharedPolicy keySharedPolicy) {
        if (pulsarClient == null)
            throw new RuntimeException("pulsarClient can't be null");

        this.client = pulsarClient;
        this.consumer = generateConsumer(client, conf, clz, messageListener, consumerEventListener, interceptors, keySharedPolicy);
    }

    public String getTopic() {
        return consumer.getTopic();
    }

    public String getSubscription() {
        return consumer.getSubscription();
    }

    public void unsubscribe() throws PulsarClientException {
        consumer.unsubscribe();
    }

    public CompletableFuture<Void> unsubscribeAsync() {
        return consumer.unsubscribeAsync();
    }

    public Message<T> receive() throws PulsarClientException {
        return consumer.receive();
    }

    public CompletableFuture<Message<T>> receiveAsync() {
        return consumer.receiveAsync();
    }

    public Message<T> receive(int timeout, TimeUnit unit) throws PulsarClientException {
        return consumer.receive(timeout, unit);
    }

    public Messages<T> batchReceive() throws PulsarClientException {
        return consumer.batchReceive();
    }

    public CompletableFuture<Messages<T>> batchReceiveAsync() {
        return consumer.batchReceiveAsync();
    }

    public void acknowledge(Message<?> message) throws PulsarClientException {
        consumer.acknowledge(message);
    }

    public void acknowledge(MessageId messageId) throws PulsarClientException {
        consumer.acknowledge(messageId);
    }

    public void acknowledge(Messages<?> messages) throws PulsarClientException {
        consumer.acknowledge(messages);
    }

    public void acknowledge(List<MessageId> messageIdList) throws PulsarClientException {
        consumer.acknowledge(messageIdList);
    }

    public void negativeAcknowledge(Message<?> message) {
        consumer.negativeAcknowledge(message);
    }

    public void negativeAcknowledge(MessageId messageId) {
        consumer.negativeAcknowledge(messageId);
    }

    public void negativeAcknowledge(Messages<?> messages) {
        consumer.negativeAcknowledge(messages);
    }

    public void reconsumeLater(Message<?> message, long delayTime, TimeUnit unit) throws PulsarClientException {
        consumer.reconsumeLater(message, delayTime, unit);
    }

    public void reconsumeLater(Messages<?> messages, long delayTime, TimeUnit unit) throws PulsarClientException {
        consumer.reconsumeLater(messages, delayTime, unit);
    }

    public void acknowledgeCumulative(Message<?> message) throws PulsarClientException {
        consumer.acknowledgeCumulative(message);
    }

    public void acknowledgeCumulative(MessageId messageId) throws PulsarClientException {
        consumer.acknowledgeCumulative(messageId);
    }

    public CompletableFuture<Void> acknowledgeCumulativeAsync(MessageId messageId, Transaction txn) {
        return consumer.acknowledgeCumulativeAsync(messageId, txn);
    }

    public void reconsumeLaterCumulative(Message<?> message, long delayTime, TimeUnit unit) throws PulsarClientException {
        consumer.reconsumeLaterCumulative(message, delayTime, unit);
    }

    public CompletableFuture<Void> acknowledgeAsync(Message<?> message) {
        return consumer.acknowledgeAsync(message);
    }

    public CompletableFuture<Void> acknowledgeAsync(MessageId messageId) {
        return consumer.acknowledgeAsync(messageId);
    }

    public CompletableFuture<Void> acknowledgeAsync(MessageId messageId, Transaction txn) {
        return consumer.acknowledgeAsync(messageId, txn);
    }

    public CompletableFuture<Void> acknowledgeAsync(Messages<?> messages) {
        return consumer.acknowledgeAsync(messages);
    }

    public CompletableFuture<Void> acknowledgeAsync(List<MessageId> messageIdList) {
        return consumer.acknowledgeAsync(messageIdList);
    }

    public CompletableFuture<Void> reconsumeLaterAsync(Message<?> message, long delayTime, TimeUnit unit) {
        return consumer.reconsumeLaterAsync(message, delayTime, unit);
    }

    public CompletableFuture<Void> reconsumeLaterAsync(Messages<?> messages, long delayTime, TimeUnit unit) {
        return consumer.reconsumeLaterAsync(messages, delayTime, unit);
    }

    public CompletableFuture<Void> acknowledgeCumulativeAsync(Message<?> message) {
        return consumer.acknowledgeCumulativeAsync(message);
    }

    public CompletableFuture<Void> acknowledgeCumulativeAsync(MessageId messageId) {
        return consumer.acknowledgeCumulativeAsync(messageId);
    }

    public CompletableFuture<Void> reconsumeLaterCumulativeAsync(Message<?> message, long delayTime, TimeUnit unit) {
        return consumer.reconsumeLaterCumulativeAsync(message, delayTime, unit);
    }

    public ConsumerStats getStats() {
        return consumer.getStats();
    }

    public void close() throws PulsarClientException {
        consumer.close();
        client.close();
    }

    public CompletableFuture<Void> closeAsync() {
        return consumer.closeAsync()
                .thenApplyAsync(v -> client.closeAsync())
                .thenAcceptAsync(v ->
                        LOGGER.warn("consumer shutdownAsync ...")
                )
                .exceptionally(e -> {
                    LOGGER.error("consumer shutdownAsync failed, cause e = {0}", e);
                    throw new RuntimeException("consumer shutdownAsync failed, cause e = " + e);
                });
    }

    public boolean hasReachedEndOfTopic() {
        return consumer.hasReachedEndOfTopic();
    }

    public void redeliverUnacknowledgedMessages() {
        consumer.redeliverUnacknowledgedMessages();
    }

    public void seek(MessageId messageId) throws PulsarClientException {
        consumer.seek(messageId);
    }

    public void seek(long timestamp) throws PulsarClientException {
        consumer.seek(timestamp);
    }

    public void seek(Function<String, Object> function) throws PulsarClientException {
        consumer.seek(function);
    }

    public CompletableFuture<Void> seekAsync(Function<String, Object> function) {
        return consumer.seekAsync(function);
    }

    public CompletableFuture<Void> seekAsync(MessageId messageId) {
        return consumer.seekAsync(messageId);
    }

    public CompletableFuture<Void> seekAsync(long timestamp) {
        return consumer.seekAsync(timestamp);
    }

    public MessageId getLastMessageId() throws PulsarClientException {
        return consumer.getLastMessageId();
    }

    public CompletableFuture<MessageId> getLastMessageIdAsync() {
        return consumer.getLastMessageIdAsync();
    }

    public boolean isConnected() {
        return consumer.isConnected();
    }

    public String getConsumerName() {
        return consumer.getConsumerName();
    }

    public void pause() {
        consumer.pause();
    }

    public void resume() {
        consumer.resume();
    }

    public long getLastDisconnectedTimestamp() {
        return consumer.getLastDisconnectedTimestamp();
    }

}

package com.blue.pulsar.component;

import com.blue.pulsar.api.conf.ConsumerConf;
import org.apache.pulsar.client.api.*;
import org.apache.pulsar.client.api.transaction.Transaction;
import org.slf4j.Logger;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static com.blue.pulsar.common.PulsarCommonsGenerator.generateConsumer;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * pulsar consumer
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public final class BluePulsarConsumer<T extends Serializable> implements Consumer<T> {

    private static final Logger LOGGER = getLogger(BluePulsarConsumer.class);

    private final org.apache.pulsar.client.api.Consumer<T> pulsarConsumer;

    public BluePulsarConsumer(PulsarClient pulsarClient, ConsumerConf conf, java.util.function.Consumer<T> consumer, ConsumerEventListener consumerEventListener,
                              List<ConsumerInterceptor<T>> interceptors, KeySharedPolicy keySharedPolicy, MessageListener<T> messageListener) {
        this.pulsarConsumer = generateConsumer(pulsarClient, conf, consumer, consumerEventListener, interceptors, keySharedPolicy, messageListener);
    }

    @Override
    public String getTopic() {
        return pulsarConsumer.getTopic();
    }

    @Override
    public String getSubscription() {
        return pulsarConsumer.getSubscription();
    }

    @Override
    public void unsubscribe() {
        try {
            pulsarConsumer.unsubscribe();
        } catch (PulsarClientException e) {
            LOGGER.error("unsubscribe failed, e = {0}", e);
            throw new RuntimeException("unsubscribe failed");
        }
    }

    @Override
    public CompletableFuture<Void> unsubscribeAsync() {
        return pulsarConsumer.unsubscribeAsync();
    }

    @Override
    public Message<T> receive() {
        try {
            return pulsarConsumer.receive();
        } catch (PulsarClientException e) {
            LOGGER.error("receive failed, e = {0}", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public CompletableFuture<Message<T>> receiveAsync() {
        return pulsarConsumer.receiveAsync();
    }

    @Override
    public Message<T> receive(int timeout, TimeUnit unit) {
        try {
            return pulsarConsumer.receive(timeout, unit);
        } catch (PulsarClientException e) {
            LOGGER.error("receive failed, timeout = {}, unit = {}, e = {}", timeout, unit, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Messages<T> batchReceive() {
        try {
            return pulsarConsumer.batchReceive();
        } catch (PulsarClientException e) {
            LOGGER.error("batchReceive failed, e = {0}", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public CompletableFuture<Messages<T>> batchReceiveAsync() {
        return pulsarConsumer.batchReceiveAsync();
    }

    @Override
    public void acknowledge(Message<?> message) {
        try {
            pulsarConsumer.acknowledge(message);
        } catch (PulsarClientException e) {
            LOGGER.error("acknowledge failed, message = {}, e = {}", message, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void acknowledge(MessageId messageId) {
        try {
            pulsarConsumer.acknowledge(messageId);
        } catch (PulsarClientException e) {
            LOGGER.error("acknowledge failed, messageId = {}, e = {}", messageId, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void acknowledge(Messages<?> messages) {
        try {
            pulsarConsumer.acknowledge(messages);
        } catch (PulsarClientException e) {
            LOGGER.error("acknowledge failed, messages = {}, e = {}", messages, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void acknowledge(List<MessageId> messageIdList) {
        try {
            pulsarConsumer.acknowledge(messageIdList);
        } catch (PulsarClientException e) {
            LOGGER.error("acknowledge failed, messageIdList = {}, e = {}", messageIdList, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void negativeAcknowledge(Message<?> message) {
        pulsarConsumer.negativeAcknowledge(message);
    }

    @Override
    public void negativeAcknowledge(MessageId messageId) {
        pulsarConsumer.negativeAcknowledge(messageId);
    }

    @Override
    public void negativeAcknowledge(Messages<?> messages) {
        pulsarConsumer.negativeAcknowledge(messages);
    }

    @Override
    public void reconsumeLater(Message<?> message, long delayTime, TimeUnit unit) {
        try {
            pulsarConsumer.reconsumeLater(message, delayTime, unit);
        } catch (PulsarClientException e) {
            LOGGER.error("reconsumeLater failed, message = {}, delayTime = {}, unit = {}, e = {}", message, delayTime, unit, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void reconsumeLater(Message<?> message, Map<String, String> customProperties, long delayTime, TimeUnit unit) {
        try {
            pulsarConsumer.reconsumeLater(message, customProperties, delayTime, unit);
        } catch (PulsarClientException e) {
            LOGGER.error("reconsumeLater failed, message = {}, customProperties = {}, delayTime = {}, unit = {}, e = {}", message, customProperties, delayTime, unit, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void reconsumeLater(Messages<?> messages, long delayTime, TimeUnit unit) {
        try {
            pulsarConsumer.reconsumeLater(messages, delayTime, unit);
        } catch (PulsarClientException e) {
            LOGGER.error("reconsumeLater failed, messages = {}, delayTime = {}, unit = {}, e = {}", messages, delayTime, unit, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void acknowledgeCumulative(Message<?> message) {
        try {
            pulsarConsumer.acknowledgeCumulative(message);
        } catch (PulsarClientException e) {
            LOGGER.error("acknowledgeCumulative failed, messages = {}, e = {}", message, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void acknowledgeCumulative(MessageId messageId) {
        try {
            pulsarConsumer.acknowledgeCumulative(messageId);
        } catch (PulsarClientException e) {
            LOGGER.error("acknowledgeCumulative failed, messageId = {}, e = {}", messageId, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public CompletableFuture<Void> acknowledgeCumulativeAsync(MessageId messageId, Transaction txn) {
        return pulsarConsumer.acknowledgeCumulativeAsync(messageId, txn);
    }

    @Override
    public void reconsumeLaterCumulative(Message<?> message, long delayTime, TimeUnit unit) {
        try {
            pulsarConsumer.reconsumeLaterCumulative(message, delayTime, unit);
        } catch (PulsarClientException e) {
            LOGGER.error("reconsumeLaterCumulative failed, message = {}, delayTime = {}, unit = {}, e = {}", message, delayTime, unit, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public CompletableFuture<Void> acknowledgeAsync(Message<?> message) {
        return pulsarConsumer.acknowledgeAsync(message);
    }

    @Override
    public CompletableFuture<Void> acknowledgeAsync(MessageId messageId) {
        return pulsarConsumer.acknowledgeAsync(messageId);
    }

    @Override
    public CompletableFuture<Void> acknowledgeAsync(MessageId messageId, Transaction txn) {
        return pulsarConsumer.acknowledgeAsync(messageId, txn);
    }

    @Override
    public CompletableFuture<Void> acknowledgeAsync(Messages<?> messages) {
        return pulsarConsumer.acknowledgeAsync(messages);
    }

    @Override
    public CompletableFuture<Void> acknowledgeAsync(List<MessageId> messageIdList) {
        return pulsarConsumer.acknowledgeAsync(messageIdList);
    }

    @Override
    public CompletableFuture<Void> acknowledgeAsync(Messages<?> messages, Transaction txn) {
        return pulsarConsumer.acknowledgeAsync(messages, txn);
    }

    @Override
    public CompletableFuture<Void> acknowledgeAsync(List<MessageId> messageIdList, Transaction txn) {
        return pulsarConsumer.acknowledgeAsync(messageIdList, txn);
    }

    @Override
    public CompletableFuture<Void> reconsumeLaterAsync(Message<?> message, long delayTime, TimeUnit unit) {
        return pulsarConsumer.reconsumeLaterAsync(message, delayTime, unit);
    }

    @Override
    public CompletableFuture<Void> reconsumeLaterAsync(Message<?> message, Map<String, String> customProperties, long delayTime, TimeUnit unit) {
        return pulsarConsumer.reconsumeLaterAsync(message, customProperties, delayTime, unit);
    }

    @Override
    public CompletableFuture<Void> reconsumeLaterAsync(Messages<?> messages, long delayTime, TimeUnit unit) {
        return pulsarConsumer.reconsumeLaterAsync(messages, delayTime, unit);
    }

    @Override
    public CompletableFuture<Void> acknowledgeCumulativeAsync(Message<?> message) {
        return pulsarConsumer.acknowledgeCumulativeAsync(message);
    }

    @Override
    public CompletableFuture<Void> acknowledgeCumulativeAsync(MessageId messageId) {
        return pulsarConsumer.acknowledgeCumulativeAsync(messageId);
    }

    @Override
    public CompletableFuture<Void> reconsumeLaterCumulativeAsync(Message<?> message, long delayTime, TimeUnit unit) {
        return pulsarConsumer.reconsumeLaterCumulativeAsync(message, delayTime, unit);
    }

    @Override
    public CompletableFuture<Void> reconsumeLaterCumulativeAsync(Message<?> message, Map<String, String> customProperties, long delayTime, TimeUnit unit) {
        return pulsarConsumer.reconsumeLaterCumulativeAsync(message, customProperties, delayTime, unit);
    }

    @Override
    public ConsumerStats getStats() {
        return pulsarConsumer.getStats();
    }

    @Override
    public void close() {
        try {
            pulsarConsumer.close();
        } catch (PulsarClientException e) {
            LOGGER.error("close failed, e = {0}", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public CompletableFuture<Void> closeAsync() {
        return pulsarConsumer.closeAsync();
    }

    @Override
    public boolean hasReachedEndOfTopic() {
        return pulsarConsumer.hasReachedEndOfTopic();
    }

    @Override
    public void redeliverUnacknowledgedMessages() {
        pulsarConsumer.redeliverUnacknowledgedMessages();
    }

    @Override
    public void seek(MessageId messageId) {
        try {
            pulsarConsumer.seek(messageId);
        } catch (PulsarClientException e) {
            LOGGER.error("seek failed, messageId = {}, e = {}", messageId, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void seek(long timestamp) {
        try {
            pulsarConsumer.seek(timestamp);
        } catch (PulsarClientException e) {
            LOGGER.error("seek failed, timestamp = {}, e = {}", timestamp, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void seek(Function<String, Object> function) {
        try {
            pulsarConsumer.seek(function);
        } catch (PulsarClientException e) {
            LOGGER.error("seek failed, function = {}, e = {}", function, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public CompletableFuture<Void> seekAsync(Function<String, Object> function) {
        return pulsarConsumer.seekAsync(function);
    }

    @Override
    public CompletableFuture<Void> seekAsync(MessageId messageId) {
        return pulsarConsumer.seekAsync(messageId);
    }

    @Override
    public CompletableFuture<Void> seekAsync(long timestamp) {
        return pulsarConsumer.seekAsync(timestamp);
    }

    @Override
    public MessageId getLastMessageId() {
        try {
            return pulsarConsumer.getLastMessageId();
        } catch (PulsarClientException e) {
            LOGGER.error("getLastMessageId failed, e = {0}", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public CompletableFuture<MessageId> getLastMessageIdAsync() {
        return pulsarConsumer.getLastMessageIdAsync();
    }

    @Override
    public boolean isConnected() {
        return pulsarConsumer.isConnected();
    }

    @Override
    public String getConsumerName() {
        return pulsarConsumer.getConsumerName();
    }

    @Override
    public void pause() {
        pulsarConsumer.pause();
    }

    @Override
    public void resume() {
        pulsarConsumer.resume();
    }

    @Override
    public long getLastDisconnectedTimestamp() {
        return pulsarConsumer.getLastDisconnectedTimestamp();
    }
}
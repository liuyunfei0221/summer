package com.blue.pulsar.common;

import com.blue.pulsar.api.conf.ConsumerConf;
import org.apache.pulsar.client.api.*;
import org.slf4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.blue.pulsar.utils.PulsarCommonsGenerator.generateClient;
import static com.blue.pulsar.utils.PulsarCommonsGenerator.generateConsumer;
import static java.lang.Thread.onSpinWait;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * pulsar消费者
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "FieldCanBeLocal", "AlibabaAvoidManuallyCreateThread"})
public final class BluePulsarConsumer<T extends Serializable> {

    private static final Logger LOGGER = getLogger(BluePulsarConsumer.class);

    private final PulsarClient pulsarClient;

    private final org.apache.pulsar.client.api.Consumer<T> pulsarConsumer;

    private final PulsarConsumerTask task;

    private final List<Thread> workingThreadHolder;

    /**
     * 创建器map
     */
    private final Map<Boolean, BiFunction<Consumer<Message<T>>, org.apache.pulsar.client.api.Consumer<T>,
            Consumer<Message<T>>>> ACK_CONSUMER_GENERATOR_HOLDER = new HashMap<>(4);
    private final Map<Boolean, BiFunction<Consumer<Message<T>>, org.apache.pulsar.client.api.Consumer<T>,
            Consumer<Messages<T>>>> ACK_BATCH_CONSUMER_GENERATOR_HOLDER = new HashMap<>(4);

    /**
     * 根据ack类型获取创建器
     */
    private final Function<Boolean, BiFunction<Consumer<Message<T>>, org.apache.pulsar.client.api.Consumer<T>,
            Consumer<Message<T>>>> ACK_CONSUMER_GENERATOR = ackType -> {
        if (ackType == null)
            throw new RuntimeException("ackType can't be null");
        return ACK_CONSUMER_GENERATOR_HOLDER.get(ackType);
    };
    private final Function<Boolean, BiFunction<Consumer<Message<T>>, org.apache.pulsar.client.api.Consumer<T>,
            Consumer<Messages<T>>>> ACK_BATCH_CONSUMER_GENERATOR = ackType -> {
        if (ackType == null)
            throw new RuntimeException("ackType can't be null");
        return ACK_BATCH_CONSUMER_GENERATOR_HOLDER.get(ackType);
    };

    public BluePulsarConsumer(ConsumerConf conf, Consumer<T> consumer, MessageListener<T> messageListener, ConsumerEventListener consumerEventListener,
                              List<ConsumerInterceptor<T>> interceptors, KeySharedPolicy keySharedPolicy) {
        if (consumer == null)
            throw new RuntimeException("consumer can't null");

        this.pulsarClient = generateClient(conf);
        this.pulsarConsumer = generateConsumer(pulsarClient, conf, consumer, messageListener, consumerEventListener, interceptors, keySharedPolicy);

        ACK_CONSUMER_GENERATOR_HOLDER.put(true, NEGATIVE_ACKNOWLEDGE_CONSUMER_GENERATOR);
        ACK_CONSUMER_GENERATOR_HOLDER.put(false, AUTO_ACKNOWLEDGE_CONSUMER_GENERATOR);
        ACK_BATCH_CONSUMER_GENERATOR_HOLDER.put(true, NEGATIVE_ACKNOWLEDGE_BATCH_CONSUMER_GENERATOR);
        ACK_BATCH_CONSUMER_GENERATOR_HOLDER.put(false, AUTO_ACKNOWLEDGE_BATCH_CONSUMER_GENERATOR);

        this.task = new PulsarConsumerTask(pulsarConsumer, consumer, conf.getEnableBatchReceive(), conf.getPollDurationMills(), conf.getEnableNegativeAcknowledge());

        int workingThreads = conf.getWorkingThreads();
        workingThreadHolder = new ArrayList<>(workingThreads);

        Thread thread;
        for (int i = 0; i < workingThreads; i++) {
            //noinspection AlibabaAvoidManuallyCreateThread
            thread = new Thread(this.task);
            thread.setName("consumer working thread for topics " + conf.getTopics() + ", seq - " + i);
            thread.setDaemon(true);
            workingThreadHolder.add(thread);
        }
    }

    /**
     * wait for thread terminate
     */
    private void blockUntilWorkingDone() {
        while (workingThreadHolder.stream().anyMatch(t -> !Thread.State.TERMINATED.equals(t.getState())))
            onSpinWait();
    }

    /**
     * 开始消费
     */
    @SuppressWarnings("AlibabaAvoidManuallyCreateThread")
    public void run() {
        try {
            new Thread(() ->
                    workingThreadHolder.forEach(Thread::start)
            ).start();
            LOGGER.info("consumers working threads start ... , threads count = {}", workingThreadHolder.size());
        } catch (Exception e) {
            LOGGER.info("consumers working threads start failed, cause e = {0}", e);
            throw new RuntimeException("consumers working threads start failed, cause e = " + e);
        }
    }

    /**
     * 关闭资源
     */
    public void shutdown() {
        try {
            task.shutdown();
            blockUntilWorkingDone();
            pulsarConsumer.unsubscribe();
            pulsarConsumer.close();
            pulsarClient.close();
            LOGGER.warn("consumer shutdown ...");
        } catch (Exception e) {
            LOGGER.error("consumer shutdown failed, cause e = {0}", e);
            throw new RuntimeException("consumer shutdown failed, cause e = {}", e);
        }
    }

    /**
     * pulsar消费任务封装类
     */
    @SuppressWarnings({"FieldCanBeLocal", "JavaDoc"})
    private final class PulsarConsumerTask implements Runnable {

        private final Logger LOGGER = getLogger(PulsarConsumerTask.class);

        private final TimeUnit POLL_DURATION_UNIT = MILLISECONDS;

        private volatile boolean running = true;

        private final org.apache.pulsar.client.api.Consumer<T> pulsarConsumer;

        private Consumer<T> consumer;

        private final boolean enableBatchReceive;

        private final Integer POLL_DURATION_MILLS;

        private final Consumer<Message<T>> EVENT_CONSUMER;
        private final Consumer<Messages<T>> EVENTS_CONSUMER;

        private final Consumer<Message<T>> MESSAGE_CONSUMER = message -> {
            T data = message.getValue();
            ofNullable(data).ifPresent(consumer);
            LOGGER.info("MESSAGE_CONSUMER handle message success: message = {}, data = {}", message, data);
        };

        /**
         * 构造
         *
         * @param pulsarConsumer
         * @param consumer
         * @param pollDurationMills
         * @param enableNegativeAcknowledge
         */
        private PulsarConsumerTask(org.apache.pulsar.client.api.Consumer<T> pulsarConsumer, Consumer<T> consumer, boolean enableBatchReceive,
                                   int pollDurationMills, boolean enableNegativeAcknowledge) {
            this.pulsarConsumer = pulsarConsumer;
            this.consumer = consumer;
            this.enableBatchReceive = enableBatchReceive;
            this.POLL_DURATION_MILLS = pollDurationMills;
            this.EVENT_CONSUMER = ACK_CONSUMER_GENERATOR.apply(enableNegativeAcknowledge).apply(MESSAGE_CONSUMER, pulsarConsumer);
            this.EVENTS_CONSUMER = ACK_BATCH_CONSUMER_GENERATOR.apply(enableNegativeAcknowledge).apply(MESSAGE_CONSUMER, pulsarConsumer);
        }

        private void handle() {
            Message<T> message = null;
            while (running)
                try {
                    message = pulsarConsumer.receive(POLL_DURATION_MILLS, POLL_DURATION_UNIT);
                    EVENT_CONSUMER.accept(message);
                } catch (Exception e) {
                    LOGGER.error("handle() received failed, message = {}, e = {}", message, e);
                }
        }

        private void handleBatch() {
            Messages<T> messages = null;
            while (running)
                try {
                    messages = pulsarConsumer.batchReceive();
                    if (messages != null) {
                        EVENTS_CONSUMER.accept(messages);
                    }
                } catch (Exception e) {
                    LOGGER.error(" handleBatch() received failed, messages = {}, e = {}", messages, e);
                }
        }

        @Override
        public void run() {
            if (enableBatchReceive) {
                handleBatch();
            } else {
                handle();
            }
        }

        /**
         * 停机
         */
        void shutdown() {
            try {
                running = false;
            } catch (Exception e) {
                LOGGER.error("task shutdown failed, e = {0}", e);
            }
        }
    }

    /**
     * 手动ack创建器   ->   处理事件/不开启自动确认/手动重置偏移 !!! 手动重置offset可能导致数据重复消费问题,如有需要,请自己在消费业务端做幂等处理
     */
    private final BiFunction<
            Consumer<Message<T>>, org.apache.pulsar.client.api.Consumer<T>,
            Consumer<Message<T>>> NEGATIVE_ACKNOWLEDGE_CONSUMER_GENERATOR =
            (msgConsumer, pulsarConsumer) ->
                    message -> {
                        if (message != null)
                            try {
                                msgConsumer.accept(message);
                                try {
                                    pulsarConsumer.acknowledge(message);
                                } catch (PulsarClientException e) {
                                    LOGGER.error("NEGATIVE_ACKNOWLEDGE_CONSUMER_GENERATOR acknowledge failed: message = {}", message);
                                }
                            } catch (Exception e) {
                                LOGGER.error("NEGATIVE_ACKNOWLEDGE_CONSUMER failed: message = {}, e = {}", message, e);
                                try {
                                    pulsarConsumer.negativeAcknowledge(message);
                                } catch (Exception exception) {
                                    LOGGER.error("negativeAcknowledge failed: message = {}", message);
                                }
                            }
                    };

    /**
     * 自动ack创建器   ->   处理事件/开启自动确认/自动处理偏移/需手动捕获具体的消费异常并补偿
     */
    private final BiFunction<
            Consumer<Message<T>>, org.apache.pulsar.client.api.Consumer<T>,
            Consumer<Message<T>>> AUTO_ACKNOWLEDGE_CONSUMER_GENERATOR =
            (msgConsumer, pulsarConsumer) ->
                    message -> {
                        if (message != null)
                            try {
                                msgConsumer.accept(message);
                            } catch (Exception e) {
                                LOGGER.error("AUTO_ACKNOWLEDGE_CONSUMER failed: message = {}, e = {}", message, e);
                            } finally {
                                try {
                                    pulsarConsumer.acknowledge(message);
                                } catch (Exception exception) {
                                    LOGGER.error("AUTO_ACKNOWLEDGE_CONSUMER_GENERATOR acknowledge failed: message = {}", message);
                                }
                            }
                    };

    /**
     * 手动ack创建器   ->   处理事件/不开启自动确认/手动重置偏移 !!! 手动重置offset可能导致数据重复消费问题,如有需要,请自己在消费业务端做幂等处理
     */
    private final BiFunction<
            Consumer<Message<T>>, org.apache.pulsar.client.api.Consumer<T>,
            Consumer<Messages<T>>> NEGATIVE_ACKNOWLEDGE_BATCH_CONSUMER_GENERATOR =
            (msgConsumer, pulsarConsumer) ->
                    messages -> {
                        if (messages != null && messages.size() > 0)
                            try {
                                for (Message<T> message : messages) {
                                    msgConsumer.accept(message);
                                }
                                try {
                                    pulsarConsumer.acknowledge(messages);
                                } catch (PulsarClientException e) {
                                    LOGGER.error("NEGATIVE_ACKNOWLEDGE_CONSUMER_GENERATOR acknowledge failed: messages = {}", messages);
                                }
                            } catch (Exception e) {
                                LOGGER.error("NEGATIVE_ACKNOWLEDGE_CONSUMER failed: messages = {}, e = {}", messages, e);
                                try {
                                    pulsarConsumer.negativeAcknowledge(messages);
                                } catch (Exception exception) {
                                    LOGGER.error("negativeAcknowledge failed: messages = {}", messages);
                                }
                            }
                    };

    /**
     * 自动ack创建器   ->   处理事件/开启自动确认/自动处理偏移/需手动捕获具体的消费异常并补偿
     */
    private final BiFunction<
            Consumer<Message<T>>, org.apache.pulsar.client.api.Consumer<T>,
            Consumer<Messages<T>>> AUTO_ACKNOWLEDGE_BATCH_CONSUMER_GENERATOR =
            (msgConsumer, pulsarConsumer) ->
                    messages -> {
                        if (messages != null && messages.size() > 0)
                            try {
                                for (Message<T> message : messages) {
                                    msgConsumer.accept(message);
                                }
                            } catch (Exception e) {
                                LOGGER.error("AUTO_ACKNOWLEDGE_CONSUMER failed: messages = {}, e = {}", messages, e);
                            } finally {
                                try {
                                    pulsarConsumer.acknowledge(messages);
                                } catch (Exception exception) {
                                    LOGGER.error("AUTO_ACKNOWLEDGE_CONSUMER_GENERATOR acknowledge failed: messages = {}", messages);
                                }
                            }
                    };

}

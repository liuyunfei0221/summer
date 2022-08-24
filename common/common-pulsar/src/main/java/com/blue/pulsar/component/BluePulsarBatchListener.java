package com.blue.pulsar.component;

import com.blue.pulsar.api.conf.ConsumerConf;
import net.openhft.affinity.AffinityThreadFactory;
import org.apache.pulsar.client.api.*;
import org.slf4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadFactory;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.pulsar.common.PulsarCommonsGenerator.*;
import static java.lang.Thread.onSpinWait;
import static java.util.Optional.ofNullable;
import static net.openhft.affinity.AffinityStrategies.SAME_CORE;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * pulsar listener batch consumer
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "FieldCanBeLocal", "AlibabaAvoidManuallyCreateThread", "JavaDoc", "DuplicatedCode"})
public final class BluePulsarBatchListener<T extends Serializable> {

    private static final Logger LOGGER = getLogger(BluePulsarBatchListener.class);

    private final org.apache.pulsar.client.api.Consumer<T> pulsarConsumer;

    private final PulsarConsumerTask task;

    private final List<Thread> workingThreadHolder;

    private static final String THREAD_NAME_PREFIX = "batch consumer working thread for topics ";

    /**
     * consumers holder
     */
    private final Map<Boolean, Function<org.apache.pulsar.client.api.Consumer<T>,
            Consumer<Messages<T>>>> ACK_BATCH_CONSUMER_GENERATOR_HOLDER = new HashMap<>(4, 2.0f);

    /**
     * get consumer generator by actType
     */
    private final Function<Boolean, Function<org.apache.pulsar.client.api.Consumer<T>,
            Consumer<Messages<T>>>> ACK_BATCH_CONSUMER_GENERATOR = ackType -> {
        if (ackType != null)
            return ACK_BATCH_CONSUMER_GENERATOR_HOLDER.get(ackType);

        throw new RuntimeException("data or params can't be null");
    };

    private Consumer<List<T>> consumer;

    private final Consumer<Messages<T>> MESSAGES_CONSUMER = messages -> {
        int size;
        if (messages != null && (size = messages.size()) > 0) {
            List<T> data = new ArrayList<>(size);
            for (Message<T> message : messages)
                ofNullable(message).map(Message::getValue).ifPresent(data::add);

            consumer.accept(data);
            LOGGER.info("MESSAGES_CONSUMER handle messages success: messages = {}, data = {}", messages, data);
        }
    };

    public BluePulsarBatchListener(PulsarClient pulsarClient, ConsumerConf conf, Consumer<List<T>> consumer, Class<T> type, ConsumerEventListener consumerEventListener,
                                   List<ConsumerInterceptor<T>> interceptors, KeySharedPolicy keySharedPolicy) {
        if (isNull(type))
            throw new RuntimeException("type can't be null");

        this.pulsarConsumer = generateConsumer(pulsarClient, conf, type, consumerEventListener, interceptors, keySharedPolicy, null);

        ACK_BATCH_CONSUMER_GENERATOR_HOLDER.put(true, NEGATIVE_ACKNOWLEDGE_BATCH_CONSUMER_GENERATOR);
        ACK_BATCH_CONSUMER_GENERATOR_HOLDER.put(false, AUTO_ACKNOWLEDGE_BATCH_CONSUMER_GENERATOR);

        this.consumer = consumer;
        this.task = new PulsarConsumerTask(pulsarConsumer, conf.getEnableNegativeAcknowledge());

        int workingThreads = conf.getWorkingThreads();
        workingThreadHolder = new ArrayList<>(workingThreads);

        ThreadFactory threadFactory = new AffinityThreadFactory(THREAD_NAME_PREFIX + conf.getTopics(), SAME_CORE);

        Thread thread;
        for (int i = 0; i < workingThreads; i++) {
            thread = threadFactory.newThread(this.task);
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
     * begin
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
     * stop
     */
    public void shutdown() {
        try {
            task.shutdown();
            blockUntilWorkingDone();
            pulsarConsumer.unsubscribe();
            pulsarConsumer.close();
            LOGGER.warn("consumer shutdown ...");
        } catch (Exception e) {
            LOGGER.error("consumer shutdown failed, cause e = {0}", e);
            throw new RuntimeException("consumer shutdown failed, cause e = {}", e);
        }
    }


    /**
     * pulsar batch consumer task
     */
    @SuppressWarnings({"FieldCanBeLocal", "JavaDoc"})
    private final class PulsarConsumerTask implements Runnable {

        private final Logger LOGGER = getLogger(PulsarConsumerTask.class);

        private volatile boolean running = true;

        private final org.apache.pulsar.client.api.Consumer<T> pulsarConsumer;

        private final Consumer<Messages<T>> EVENTS_CONSUMER;

        /**
         * constructor
         *
         * @param pulsarConsumer
         * @param enableNegativeAcknowledge
         */
        private PulsarConsumerTask(org.apache.pulsar.client.api.Consumer<T> pulsarConsumer, boolean enableNegativeAcknowledge) {
            this.pulsarConsumer = pulsarConsumer;
            this.EVENTS_CONSUMER = ACK_BATCH_CONSUMER_GENERATOR.apply(enableNegativeAcknowledge).apply(pulsarConsumer);
        }

        private void handle() {
            Messages<T> messages = null;
            while (running)
                try {
                    if ((messages = pulsarConsumer.batchReceive()) != null && messages.size() > 0) {
                        EVENTS_CONSUMER.accept(messages);
                    } else {
                        onSpinWait();
                    }
                } catch (PulsarClientException e) {
                    LOGGER.error(" handleBatch() received failed, messages = {}, e = {}", messages, e);
                    onSpinWait();
                }
        }

        @Override
        public void run() {
            handle();
        }

        /**
         * stop
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
     * manual ack -> Handling events/not enabling automatic confirmation/manually resetting the offset !!! Manually resetting the offset may cause the problem of repeated consumption of data. If necessary,
     * please do idempotent processing on the consumer business side by yourself
     */
    private final Function<org.apache.pulsar.client.api.Consumer<T>, Consumer<Messages<T>>> NEGATIVE_ACKNOWLEDGE_BATCH_CONSUMER_GENERATOR =
            pulsarConsumer ->
                    messages -> {
                        if (messages != null && messages.size() > 0)
                            try {
                                MESSAGES_CONSUMER.accept(messages);
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
     * auto ack -> Handling events/enabling automatic confirmation/automatically handle offsets/need to manually capture specific consumption exceptions and compensate
     */
    private final Function<org.apache.pulsar.client.api.Consumer<T>, Consumer<Messages<T>>> AUTO_ACKNOWLEDGE_BATCH_CONSUMER_GENERATOR =
            pulsarConsumer ->
                    messages -> {
                        if (messages != null && messages.size() > 0)
                            try {
                                MESSAGES_CONSUMER.accept(messages);
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

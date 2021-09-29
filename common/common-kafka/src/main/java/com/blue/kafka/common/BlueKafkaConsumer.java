package com.blue.kafka.common;

import com.blue.kafka.api.conf.ConsumerConf;
import com.google.gson.Gson;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;

import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.blue.kafka.utils.AuthProcessor.authForConsumer;
import static com.blue.kafka.utils.FunctionParameterClzGetter.getConsumerParameterType;
import static com.blue.kafka.utils.GsonFactory.getGson;
import static java.lang.String.valueOf;
import static java.lang.Thread.onSpinWait;
import static java.time.Duration.ofMillis;
import static java.util.Optional.ofNullable;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * kafka consumer
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "rawtypes", "unchecked", "AliControlFlowStatementWithoutBraces", "JavaDoc", "AlibabaAvoidManuallyCreateThread"})
public final class BlueKafkaConsumer<T extends Serializable> {

    private static final Logger LOGGER = getLogger(BlueKafkaConsumer.class);

    org.apache.kafka.clients.consumer.KafkaConsumer kafkaConsumer;

    private final KafkaConsumerTask task;

    private final List<Thread> workingThreadHolder;

    private Class<T> clz;

    private static final Gson GSON = getGson();

    /**
     * record consumer generator
     */
    @SuppressWarnings("FieldCanBeLocal")
    private final Function<Consumer<T>, Consumer<ConsumerRecord<String, String>>> RECORD_CONSUMER_GENERATOR = consumer ->
            record ->
                    ofNullable(record.value())
                            .ifPresent(data -> {
                                T t = GSON.fromJson(data, clz);
                                consumer.accept(t);
                                LOGGER.warn("data consume successes, data = {}", data);
                            });

    /**
     * records consumer generator
     *
     * @param recordConsumer
     * @param kafkaConsumer
     * @param topic
     * @param enableAutoCommit
     * @return
     */
    private Consumer<ConsumerRecords<String, String>> generateRecordsConsumer(Consumer<ConsumerRecord<String, String>> recordConsumer, org.apache.kafka.clients.consumer.KafkaConsumer kafkaConsumer, String topic, boolean enableAutoCommit) {
        return enableAutoCommit ?
                records -> {
                    Iterable<ConsumerRecord<String, String>> recordsIte = records.records(topic);
                    for (ConsumerRecord<String, String> record : recordsIte)
                        recordConsumer.accept(record);
                }
                :
                records -> {
                    Iterable<ConsumerRecord<String, String>> recordsIte = records.records(topic);
                    for (ConsumerRecord<String, String> record : recordsIte)
                        recordConsumer.accept(record);
                    kafkaConsumer.commitSync();
                };
    }

    /**
     * kafka consumer generator
     */
    private static final Function<ConsumerConf, org.apache.kafka.clients.consumer.KafkaConsumer> KAFKA_CONSUMER_GENERATOR = consumerConf -> {
        Properties configs = new Properties();

        authForConsumer(configs, consumerConf);

        configs.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerConf.getBootstrap());
        configs.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, valueOf(consumerConf.getEnableAutoCommit()));
        configs.setProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, valueOf(consumerConf.getAutoCommitIntervalMs()));
        configs.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, consumerConf.getAutoOffsetReset());
        configs.setProperty(ConsumerConfig.GROUP_ID_CONFIG, consumerConf.getGroupId() + ofNullable(consumerConf.getGroupSequence()).orElse(""));

        Deserializer<String> stringDeserializer = new StringDeserializer();

        org.apache.kafka.clients.consumer.KafkaConsumer consumer = new org.apache.kafka.clients.consumer.KafkaConsumer(configs, stringDeserializer, stringDeserializer);
        consumer.subscribe(Collections.singletonList(consumerConf.getTopic()));

        return consumer;
    };


    public BlueKafkaConsumer(ConsumerConf consumerConf, Consumer<T> consumer) {
        //noinspection unchecked
        this.clz = (Class<T>) getConsumerParameterType(consumer);

        Consumer<ConsumerRecord<String, String>> recordConsumer = RECORD_CONSUMER_GENERATOR.apply(consumer);
        this.kafkaConsumer = KAFKA_CONSUMER_GENERATOR.apply(consumerConf);
        Consumer<ConsumerRecords<String, String>> recordsConsumer = generateRecordsConsumer(recordConsumer, kafkaConsumer, consumerConf.getTopic(), consumerConf.getEnableAutoCommit());

        this.task = new KafkaConsumerTask(kafkaConsumer, recordsConsumer, consumerConf.getPollDurationMills());

        Integer workingThreads = consumerConf.getWorkingThreads();

        workingThreadHolder = new ArrayList<>(workingThreads);

        Thread thread;
        for (int i = 0; i < workingThreads; i++) {
            //noinspection AlibabaAvoidManuallyCreateThread
            thread = new Thread(this.task);
            thread.setName("consumer working thread for topics " + consumerConf.getTopic() + ", seq - " + i);
            thread.setDaemon(true);
            workingThreadHolder.add(thread);
        }

    }

    /**
     * begin
     */
    @SuppressWarnings("AlibabaAvoidManuallyCreateThread")
    public void run() {
        new Thread(() -> {
            workingThreadHolder.forEach(Thread::start);
            LOGGER.info("consumer working threads start");
        }).start();
    }

    /**
     * stop
     */
    public void shutdown() {
        try {
            this.task.shutdown();
            blockUntilWorkingDone();
            this.kafkaConsumer.unsubscribe();
            this.kafkaConsumer.close();
            LOGGER.info("consumer shutdown ...");
        } catch (Exception e) {
            LOGGER.error("consumer shutdown failed, cause e = {0}", e);
            throw new RuntimeException("consumer shutdown failed, cause e = {}", e);
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
     * kafka consumer task
     */
    private static final class KafkaConsumerTask implements Runnable {

        private final Logger LOGGER = getLogger(KafkaConsumerTask.class);

        private volatile boolean running;

        private final org.apache.kafka.clients.consumer.KafkaConsumer kafkaConsumer;

        private final Consumer<ConsumerRecords<String, String>> recordsConsumer;

        private final Duration pollDuration;

        public KafkaConsumerTask(org.apache.kafka.clients.consumer.KafkaConsumer kafkaConsumer, Consumer<ConsumerRecords<String, String>> recordsConsumer, int pollDurationMills) {
            if (kafkaConsumer == null || recordsConsumer == null)
                throw new RuntimeException("kafkaConsumer or recordsConsumer can't be null");

            this.running = true;
            this.kafkaConsumer = kafkaConsumer;
            this.recordsConsumer = recordsConsumer;

            this.pollDuration = ofMillis(pollDurationMills);
        }

        @Override
        public void run() {
            ConsumerRecords<String, String> records = null;
            while (running)
                try {
                    records = kafkaConsumer.poll(pollDuration);
                    this.recordsConsumer.accept(records);
                } catch (Exception e) {
                    LOGGER.error("run() failed,records = {}, e = {}", records, e);
                }
        }

        public void shutdown() {
            try {
                this.running = false;
                LOGGER.info("task shutdown ...");
            } catch (Exception e) {
                LOGGER.error("task shutdown failed, e = {0}", e);
            }
        }
    }

}

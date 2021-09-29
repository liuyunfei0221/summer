package com.blue.kafka.api.generator;

import com.blue.kafka.api.conf.ConsumerConf;
import com.blue.kafka.common.BlueKafkaConsumer;

import java.io.Serializable;
import java.util.function.Consumer;


/**
 * kafka consumer generator
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "DuplicatedCode", "unused", "AliControlFlowStatementWithoutBraces"})
public final class BlueKafkaConsumerGenerator {

    /**
     * generate consumer
     *
     * @param consumerConf
     * @param consumer
     * @param <T>
     * @return
     */
    public static <T extends Serializable> BlueKafkaConsumer<T> generateSummerConsumer(ConsumerConf consumerConf, Consumer<T> consumer) {
        assertParams(consumerConf, consumer);
        return new BlueKafkaConsumer<>(consumerConf, consumer);
    }

    /**
     * assert params
     *
     * @param consumerConf
     * @param consumer
     * @param <T>
     */
    private static <T extends Serializable> void assertParams(ConsumerConf consumerConf, Consumer<T> consumer) {
        String bootstrap = consumerConf.getBootstrap();
        if (bootstrap == null || "".equals(bootstrap))
            throw new RuntimeException("bootstrap can't be null or ''");

        String topic = consumerConf.getTopic();
        if (topic == null || "".equals(topic))
            throw new RuntimeException("topic can't be null or ''");

        String groupId = consumerConf.getGroupId();
        if (groupId == null || "".equals(groupId))
            throw new RuntimeException("groupId can't be null or ''");

        Boolean enableAutoCommit = consumerConf.getEnableAutoCommit();
        if (enableAutoCommit == null)
            throw new RuntimeException("enableAutoCommit can't be null");

        String autoOffsetReset = consumerConf.getAutoOffsetReset();
        if (autoOffsetReset == null || "".equals(autoOffsetReset))
            throw new RuntimeException("autoOffsetReset can't be null or ''");

        Integer autoCommitIntervalMs = consumerConf.getAutoCommitIntervalMs();
        if (autoCommitIntervalMs == null || autoCommitIntervalMs < 0)
            throw new RuntimeException("autoCommitIntervalMs can't be null or less than 0");

        Integer pollDurationMills = consumerConf.getPollDurationMills();
        if (pollDurationMills == null || pollDurationMills < 1L)
            throw new RuntimeException("pollDurationMills can't be null or less than 1");

        Integer workingThreads = consumerConf.getWorkingThreads();
        if (workingThreads == null || workingThreads < 1)
            throw new RuntimeException("workingThreads can't be null or less than 1");
    }

}

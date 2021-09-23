package com.blue.kafka.api.generator;

import com.blue.kafka.api.conf.ProducerConf;
import com.blue.kafka.common.BlueKafkaProducer;
import org.springframework.kafka.support.SendResult;

import java.io.Serializable;
import java.util.function.BiConsumer;


/**
 * kafka消费者构建工厂
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "DuplicatedCode", "unused", "AliControlFlowStatementWithoutBraces"})
public final class BlueKafkaProducerGenerator {

    /**
     * 构建kafka模板
     *
     * @param producerConf
     * @return
     */
    public static <T extends Serializable> BlueKafkaProducer<T> generateProducer(ProducerConf producerConf, BiConsumer<SendResult<String, String>, T> successHandler, BiConsumer<Throwable, T> failHandler) {
        if (producerConf == null)
            throw new RuntimeException("summerProducerDeploy can't be null");

        String bootstrap = producerConf.getBootstrap();
        if (bootstrap == null || "".equals(bootstrap))
            throw new RuntimeException("bootstrap can't be null or ''");

        String topic = producerConf.getTopic();
        if (topic == null || "".equals(topic))
            throw new RuntimeException("topic can't be null or ''");

        String groupId = producerConf.getGroupId();
        if (groupId == null || "".equals(groupId))
            throw new RuntimeException("groupId can't be null or ''");

        String acks = producerConf.getAcks();
        if (acks == null || "".equals(acks))
            throw new RuntimeException("acks can't be null or ''");

        Integer retries = producerConf.getRetries();
        if (retries == null || retries < 0)
            throw new RuntimeException("autoCommitIntervalMs can't be null or less than 0");

        Integer batchSize = producerConf.getBatchSize();
        if (batchSize == null || batchSize < 0)
            throw new RuntimeException("batchSize can't be null or less than 0");

        Integer lingerMs = producerConf.getLingerMs();
        if (lingerMs == null || lingerMs < 0)
            throw new RuntimeException("lingerMs can't be null or less than 0");

        Integer bufferMemory = producerConf.getBufferMemory();
        if (bufferMemory == null || bufferMemory < 0)
            throw new RuntimeException("bufferMemory can't be null or less than 0");

        return new BlueKafkaProducer<>(producerConf, successHandler, failHandler);
    }


}

package com.blue.kafka.common;

import com.blue.kafka.api.conf.ProducerConf;
import com.google.gson.Gson;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static com.blue.kafka.utils.AuthProcessor.authForProducer;
import static com.blue.kafka.utils.GsonFactory.getGson;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * kafka单发送实例
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused"})
public final class BlueKafkaProducer<T extends Serializable> {

    private static final Logger LOGGER = getLogger(BlueKafkaProducer.class);

    private final String topic;

    private static final Gson GSON = getGson();

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final BiConsumer<SendResult<String, String>, T> successHandler;

    private final BiConsumer<Throwable, T> failHandler;

    public BlueKafkaProducer(ProducerConf producerConf, BiConsumer<SendResult<String, String>, T> successHandler, BiConsumer<Throwable, T> failHandler) {
        this.topic = producerConf.getTopic();

        Map<String, Object> configs = new HashMap<>();

        authForProducer(configs, producerConf);

        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, producerConf.getBootstrap());
        configs.put(ProducerConfig.ACKS_CONFIG, producerConf.getAcks());
        configs.put(ProducerConfig.RETRIES_CONFIG, producerConf.getRetries());
        configs.put(ProducerConfig.BATCH_SIZE_CONFIG, producerConf.getBatchSize());
        configs.put(ProducerConfig.LINGER_MS_CONFIG, producerConf.getLingerMs());
        configs.put(ProducerConfig.BUFFER_MEMORY_CONFIG, producerConf.getBufferMemory());

        DefaultKafkaProducerFactory<String, String> producerFactory = new DefaultKafkaProducerFactory<>(configs, StringSerializer::new, StringSerializer::new);
        this.kafkaTemplate = new KafkaTemplate<>(producerFactory);

        this.successHandler = successHandler;
        this.failHandler = failHandler;
    }


    /**
     * 发送数据
     *
     * @param data
     */
    public void send(T data) {
        //发送消息
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, GSON.toJson(data));
        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onSuccess(SendResult<String, String> sendResult) {
                LOGGER.warn("sendMessage success --> sendResult = {}, data = {}", sendResult, data);
                successHandler.accept(sendResult, data);
            }

            @SuppressWarnings("NullableProblems")
            @Override
            public void onFailure(Throwable throwable) {
                LOGGER.error("sendMessage failed --> throwable = {}, data = {}", throwable.getMessage(), data);
                failHandler.accept(throwable, data);
            }
        });
    }

}

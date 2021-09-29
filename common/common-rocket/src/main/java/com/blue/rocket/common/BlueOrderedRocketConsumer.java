package com.blue.rocket.common;

import com.blue.rocket.api.conf.ConsumerConf;
import com.google.gson.Gson;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.protocol.LanguageCode;
import org.slf4j.Logger;

import java.nio.charset.Charset;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.blue.rocket.utils.FunctionParameterClzGetter.getConsumerParameterType;
import static com.blue.rocket.utils.GsonFactory.getGson;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * rocket ordered consumer
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "FieldCanBeLocal", "PlaceholderCountMatchesArgumentCount", "DuplicatedCode"})
public final class BlueOrderedRocketConsumer<T> {

    private static final Logger LOGGER = getLogger(BlueOrderedRocketConsumer.class);

    private static final Gson GSON = getGson();

    private final Charset CHARSET = UTF_8;

    private final String topic;

    private Class<T> clz;

    private final Consumer<List<MessageExt>> messageExtListConsumer;

    private final DefaultMQPushConsumer rocketConsumer;

    @SuppressWarnings("FieldCanBeLocal")
    private final Function<Consumer<T>, Consumer<List<MessageExt>>> MESSAGE_LIST_CONSUMER_GENERATOR = consumer ->
            messageExtList -> {
                String data;
                T t;
                for (MessageExt messageExt : messageExtList) {
                    data = new String(messageExt.getBody(), CHARSET);

                    t = GSON.fromJson(data, clz);
                    consumer.accept(t);

                    LOGGER.info("handleEvent success: messageExt = {}, data = {}", messageExt, data);
                }
            };


    @SuppressWarnings("FieldCanBeLocal")
    private final Function<ConsumerConf, DefaultMQPushConsumer> ROCKET_CONSUMER_GENERATOR = conf -> {
        DefaultMQPushConsumer rocketConsumer = new DefaultMQPushConsumer();

        rocketConsumer.setNamespace(conf.getNamespace());
        try {
            rocketConsumer.subscribe(conf.getTopic(), conf.getSubExpression());
        } catch (MQClientException e) {
            throw new RuntimeException("rocketConsumer.subscribe(conf.getTopic(), conf.getSubExpression()) failed, e = {}", e);
        }

        rocketConsumer.setClientIP(conf.getClientIP());
        rocketConsumer.setInstanceName(conf.getInstanceName());
        rocketConsumer.setClientCallbackExecutorThreads(conf.getClientCallbackExecutorThreads());
        rocketConsumer.setNamespace(conf.getNamespace());
        rocketConsumer.setAccessChannel(conf.getAccessChannel());
        rocketConsumer.setPollNameServerInterval(conf.getPollNameServerInterval());
        rocketConsumer.setHeartbeatBrokerInterval(conf.getHeartbeatBrokerInterval());
        rocketConsumer.setPersistConsumerOffsetInterval(conf.getPersistConsumerOffsetInterval());
        rocketConsumer.setPullTimeDelayMillsWhenException(conf.getPullTimeDelayMillsWhenException());
        rocketConsumer.setUnitMode(conf.getUnitMode());
        rocketConsumer.setUnitName(conf.getUnitName());
        rocketConsumer.setVipChannelEnabled(conf.getVipChannelEnabled());
        rocketConsumer.setUseTLS(conf.getUseTLS());
        rocketConsumer.setLanguage(LanguageCode.JAVA);
        rocketConsumer.setConsumerGroup(conf.getConsumerGroup());

        rocketConsumer.setMessageModel(conf.getMessageModel());
        rocketConsumer.setConsumeFromWhere(conf.getConsumeFromWhere());
        rocketConsumer.setConsumeTimestamp(conf.getConsumeTimestamp());
        rocketConsumer.setConsumeThreadMin(conf.getConsumeThreadMin());
        rocketConsumer.setConsumeThreadMax(conf.getConsumeThreadMax());
        rocketConsumer.setAdjustThreadPoolNumsThreshold(conf.getAdjustThreadPoolNumsThreshold());
        rocketConsumer.setConsumeConcurrentlyMaxSpan(conf.getConsumeConcurrentlyMaxSpan());
        rocketConsumer.setPullThresholdForQueue(conf.getPullThresholdForQueue());
        rocketConsumer.setPullThresholdSizeForQueue(conf.getPullThresholdSizeForQueue());
        rocketConsumer.setPullThresholdForTopic(conf.getPullThresholdForTopic());
        rocketConsumer.setPullThresholdSizeForTopic(conf.getPullThresholdSizeForTopic());
        rocketConsumer.setPullInterval(conf.getPullInterval());

        rocketConsumer.setConsumeMessageBatchMaxSize(conf.getConsumeMessageBatchMaxSize());
        rocketConsumer.setPullBatchSize(conf.getPullBatchSize());
        rocketConsumer.setPostSubscriptionWhenPull(conf.getPostSubscriptionWhenPull());
        rocketConsumer.setMaxReconsumeTimes(conf.getMaxReconsumeTimes());
        rocketConsumer.setSuspendCurrentQueueTimeMillis(conf.getSuspendCurrentQueueTimeMillis());
        rocketConsumer.setConsumeTimeout(conf.getConsumeTimeout());
        rocketConsumer.setAwaitTerminationMillisWhenShutdown(conf.getAwaitTerminationMillisWhenShutdown());

        return rocketConsumer;
    };

    public BlueOrderedRocketConsumer(ConsumerConf conf, Consumer<T> consumer, MessageListenerOrderly messageListenerOrderly) {
        this.topic = conf.getTopic();
        //noinspection unchecked
        this.clz = (Class<T>) getConsumerParameterType(consumer);
        this.messageExtListConsumer = MESSAGE_LIST_CONSUMER_GENERATOR.apply(consumer);

        DefaultMQPushConsumer con = ROCKET_CONSUMER_GENERATOR.apply(conf);

        try {
            con.subscribe(this.topic, conf.getSubExpression());
        } catch (MQClientException e) {
            LOGGER.error("con.subscribe failed, e = {}", e);
            throw new RuntimeException("con.subscribe failed, e = " + e);
        }

        con.registerMessageListener(messageListenerOrderly);
        this.rocketConsumer = con;
    }

    /**
     * begin
     */
    public void run() {
        try {
            rocketConsumer.start();
            LOGGER.info("rocketConsumer start");
        } catch (MQClientException e) {
            LOGGER.error("rocketConsumer start failed, e = {}", e);
        }
    }

    /**
     * stop
     */
    public void shutdown() {
        try {
            this.rocketConsumer.unsubscribe(this.topic);
            this.rocketConsumer.suspend();
            this.rocketConsumer.shutdown();
            LOGGER.info("rocketConsumer shutdown");
        } catch (Exception e) {
            LOGGER.error("rocketConsumer shutdown failed, cause e = {0}", e);
            throw new RuntimeException("consumer shutdown failed, cause e = {}", e);
        }
    }

}

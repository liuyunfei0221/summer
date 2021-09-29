package com.blue.rocket.api.conf;

import org.apache.rocketmq.client.AccessChannel;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

/**
 * rocket conf
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "AlibabaLowerCamelCaseVariableNaming", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "SpellCheckingInspection"})
public interface ConsumerConf {

    String getNamesrvAddr();

    String getTopic();

    String getSubExpression();

    String getClientIP();

    String getInstanceName();

    Boolean getEnableNegativeAcknowledge();

    Integer getClientCallbackExecutorThreads();

    String getNamespace();

    AccessChannel getAccessChannel();

    Integer getPollNameServerInterval();

    Integer getHeartbeatBrokerInterval();

    Integer getPersistConsumerOffsetInterval();

    Long getPullTimeDelayMillsWhenException();

    Boolean getUnitMode();

    String getUnitName();

    Boolean getVipChannelEnabled();

    Boolean getUseTLS();

    String getConsumerGroup();

    MessageModel getMessageModel();

    ConsumeFromWhere getConsumeFromWhere();

    String getConsumeTimestamp();

    Integer getConsumeThreadMin();

    Integer getConsumeThreadMax();

    Long getAdjustThreadPoolNumsThreshold();

    Integer getConsumeConcurrentlyMaxSpan();

    Integer getPullThresholdForQueue();

    Integer getPullThresholdSizeForQueue();

    Integer getPullThresholdForTopic();

    Integer getPullThresholdSizeForTopic();

    Long getPullInterval();

    Integer getConsumeMessageBatchMaxSize();

    Integer getPullBatchSize();

    Boolean getPostSubscriptionWhenPull();

    Integer getMaxReconsumeTimes();

    Long getSuspendCurrentQueueTimeMillis();

    Long getConsumeTimeout();

    Long getAwaitTerminationMillisWhenShutdown();

}

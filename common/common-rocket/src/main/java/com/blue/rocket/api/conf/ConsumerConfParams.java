package com.blue.rocket.api.conf;

import org.apache.rocketmq.client.AccessChannel;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;


/**
 * consumer params
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "AlibabaLowerCamelCaseVariableNaming", "SpellCheckingInspection"})
public class ConsumerConfParams implements ConsumerConf {

    private String namesrvAddr;
    private String topic;
    private String subExpression;
    private String clientIP;
    private String instanceName;
    private Boolean enableNegativeAcknowledge;
    private Integer clientCallbackExecutorThreads;
    private String namespace;
    private AccessChannel accessChannel;
    private Integer pollNameServerInterval;
    private Integer heartbeatBrokerInterval;
    private Integer persistConsumerOffsetInterval;
    private Long pullTimeDelayMillsWhenException;
    private Boolean unitMode;
    private String unitName;
    private Boolean vipChannelEnabled;
    private Boolean useTLS;

    private String consumerGroup;
    private MessageModel messageModel;
    private ConsumeFromWhere consumeFromWhere;
    private String consumeTimestamp;
    private Integer consumeThreadMin;
    private Integer consumeThreadMax;
    private Long adjustThreadPoolNumsThreshold;
    private Integer consumeConcurrentlyMaxSpan;
    private Integer pullThresholdForQueue;
    private Integer pullThresholdSizeForQueue;
    private Integer pullThresholdForTopic;
    private Integer pullThresholdSizeForTopic;
    private Long pullInterval;
    private Integer consumeMessageBatchMaxSize;
    private Integer pullBatchSize;
    private Boolean postSubscriptionWhenPull;
    private Integer maxReconsumeTimes;
    private Long suspendCurrentQueueTimeMillis;
    private Long consumeTimeout;
    private Long awaitTerminationMillisWhenShutdown;

    @Override
    public String getNamesrvAddr() {
        return namesrvAddr;
    }

    public void setNamesrvAddr(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
    }

    @Override
    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public String getSubExpression() {
        return subExpression;
    }

    public void setSubExpression(String subExpression) {
        this.subExpression = subExpression;
    }

    @Override
    public String getClientIP() {
        return clientIP;
    }

    public void setClientIP(String clientIP) {
        this.clientIP = clientIP;
    }

    @Override
    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    @Override
    public Boolean getEnableNegativeAcknowledge() {
        return enableNegativeAcknowledge;
    }

    public void setEnableNegativeAcknowledge(Boolean enableNegativeAcknowledge) {
        this.enableNegativeAcknowledge = enableNegativeAcknowledge;
    }

    @Override
    public Integer getClientCallbackExecutorThreads() {
        return clientCallbackExecutorThreads;
    }

    public void setClientCallbackExecutorThreads(Integer clientCallbackExecutorThreads) {
        this.clientCallbackExecutorThreads = clientCallbackExecutorThreads;
    }

    @Override
    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public AccessChannel getAccessChannel() {
        return accessChannel;
    }

    public void setAccessChannel(AccessChannel accessChannel) {
        this.accessChannel = accessChannel;
    }

    @Override
    public Integer getPollNameServerInterval() {
        return pollNameServerInterval;
    }

    public void setPollNameServerInterval(Integer pollNameServerInterval) {
        this.pollNameServerInterval = pollNameServerInterval;
    }

    @Override
    public Integer getHeartbeatBrokerInterval() {
        return heartbeatBrokerInterval;
    }

    public void setHeartbeatBrokerInterval(Integer heartbeatBrokerInterval) {
        this.heartbeatBrokerInterval = heartbeatBrokerInterval;
    }

    @Override
    public Integer getPersistConsumerOffsetInterval() {
        return persistConsumerOffsetInterval;
    }

    public void setPersistConsumerOffsetInterval(Integer persistConsumerOffsetInterval) {
        this.persistConsumerOffsetInterval = persistConsumerOffsetInterval;
    }

    @Override
    public Long getPullTimeDelayMillsWhenException() {
        return pullTimeDelayMillsWhenException;
    }

    public void setPullTimeDelayMillsWhenException(Long pullTimeDelayMillsWhenException) {
        this.pullTimeDelayMillsWhenException = pullTimeDelayMillsWhenException;
    }

    @Override
    public Boolean getUnitMode() {
        return unitMode;
    }

    public void setUnitMode(Boolean unitMode) {
        this.unitMode = unitMode;
    }

    @Override
    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    @Override
    public Boolean getVipChannelEnabled() {
        return vipChannelEnabled;
    }

    public void setVipChannelEnabled(Boolean vipChannelEnabled) {
        this.vipChannelEnabled = vipChannelEnabled;
    }

    @Override
    public Boolean getUseTLS() {
        return useTLS;
    }

    public void setUseTLS(Boolean useTLS) {
        this.useTLS = useTLS;
    }

    @Override
    public String getConsumerGroup() {
        return consumerGroup;
    }

    public void setConsumerGroup(String consumerGroup) {
        this.consumerGroup = consumerGroup;
    }

    @Override
    public MessageModel getMessageModel() {
        return messageModel;
    }

    public void setMessageModel(MessageModel messageModel) {
        this.messageModel = messageModel;
    }

    @Override
    public ConsumeFromWhere getConsumeFromWhere() {
        return consumeFromWhere;
    }

    public void setConsumeFromWhere(ConsumeFromWhere consumeFromWhere) {
        this.consumeFromWhere = consumeFromWhere;
    }

    @Override
    public String getConsumeTimestamp() {
        return consumeTimestamp;
    }

    public void setConsumeTimestamp(String consumeTimestamp) {
        this.consumeTimestamp = consumeTimestamp;
    }

    @Override
    public Integer getConsumeThreadMin() {
        return consumeThreadMin;
    }

    public void setConsumeThreadMin(Integer consumeThreadMin) {
        this.consumeThreadMin = consumeThreadMin;
    }

    @Override
    public Integer getConsumeThreadMax() {
        return consumeThreadMax;
    }

    public void setConsumeThreadMax(Integer consumeThreadMax) {
        this.consumeThreadMax = consumeThreadMax;
    }

    @Override
    public Long getAdjustThreadPoolNumsThreshold() {
        return adjustThreadPoolNumsThreshold;
    }

    public void setAdjustThreadPoolNumsThreshold(Long adjustThreadPoolNumsThreshold) {
        this.adjustThreadPoolNumsThreshold = adjustThreadPoolNumsThreshold;
    }

    @Override
    public Integer getConsumeConcurrentlyMaxSpan() {
        return consumeConcurrentlyMaxSpan;
    }

    public void setConsumeConcurrentlyMaxSpan(Integer consumeConcurrentlyMaxSpan) {
        this.consumeConcurrentlyMaxSpan = consumeConcurrentlyMaxSpan;
    }

    @Override
    public Integer getPullThresholdForQueue() {
        return pullThresholdForQueue;
    }

    public void setPullThresholdForQueue(Integer pullThresholdForQueue) {
        this.pullThresholdForQueue = pullThresholdForQueue;
    }

    @Override
    public Integer getPullThresholdSizeForQueue() {
        return pullThresholdSizeForQueue;
    }

    public void setPullThresholdSizeForQueue(Integer pullThresholdSizeForQueue) {
        this.pullThresholdSizeForQueue = pullThresholdSizeForQueue;
    }

    @Override
    public Integer getPullThresholdForTopic() {
        return pullThresholdForTopic;
    }

    public void setPullThresholdForTopic(Integer pullThresholdForTopic) {
        this.pullThresholdForTopic = pullThresholdForTopic;
    }

    @Override
    public Integer getPullThresholdSizeForTopic() {
        return pullThresholdSizeForTopic;
    }

    public void setPullThresholdSizeForTopic(Integer pullThresholdSizeForTopic) {
        this.pullThresholdSizeForTopic = pullThresholdSizeForTopic;
    }

    @Override
    public Long getPullInterval() {
        return pullInterval;
    }

    public void setPullInterval(Long pullInterval) {
        this.pullInterval = pullInterval;
    }

    @Override
    public Integer getConsumeMessageBatchMaxSize() {
        return consumeMessageBatchMaxSize;
    }

    public void setConsumeMessageBatchMaxSize(Integer consumeMessageBatchMaxSize) {
        this.consumeMessageBatchMaxSize = consumeMessageBatchMaxSize;
    }

    @Override
    public Integer getPullBatchSize() {
        return pullBatchSize;
    }

    public void setPullBatchSize(Integer pullBatchSize) {
        this.pullBatchSize = pullBatchSize;
    }

    @Override
    public Boolean getPostSubscriptionWhenPull() {
        return postSubscriptionWhenPull;
    }

    public void setPostSubscriptionWhenPull(Boolean postSubscriptionWhenPull) {
        this.postSubscriptionWhenPull = postSubscriptionWhenPull;
    }

    @Override
    public Integer getMaxReconsumeTimes() {
        return maxReconsumeTimes;
    }

    public void setMaxReconsumeTimes(Integer maxReconsumeTimes) {
        this.maxReconsumeTimes = maxReconsumeTimes;
    }

    @Override
    public Long getSuspendCurrentQueueTimeMillis() {
        return suspendCurrentQueueTimeMillis;
    }

    public void setSuspendCurrentQueueTimeMillis(Long suspendCurrentQueueTimeMillis) {
        this.suspendCurrentQueueTimeMillis = suspendCurrentQueueTimeMillis;
    }

    @Override
    public Long getConsumeTimeout() {
        return consumeTimeout;
    }

    public void setConsumeTimeout(Long consumeTimeout) {
        this.consumeTimeout = consumeTimeout;
    }

    @Override
    public Long getAwaitTerminationMillisWhenShutdown() {
        return awaitTerminationMillisWhenShutdown;
    }

    public void setAwaitTerminationMillisWhenShutdown(Long awaitTerminationMillisWhenShutdown) {
        this.awaitTerminationMillisWhenShutdown = awaitTerminationMillisWhenShutdown;
    }

    @Override
    public String toString() {
        return "RocketConsumerParams{" +
                "namesrvAddr='" + namesrvAddr + '\'' +
                ", topic='" + topic + '\'' +
                ", subExpression='" + subExpression + '\'' +
                ", clientIP='" + clientIP + '\'' +
                ", instanceName='" + instanceName + '\'' +
                ", enableNegativeAcknowledge=" + enableNegativeAcknowledge +
                ", clientCallbackExecutorThreads=" + clientCallbackExecutorThreads +
                ", namespace='" + namespace + '\'' +
                ", accessChannel=" + accessChannel +
                ", pollNameServerInterval=" + pollNameServerInterval +
                ", heartbeatBrokerInterval=" + heartbeatBrokerInterval +
                ", persistConsumerOffsetInterval=" + persistConsumerOffsetInterval +
                ", pullTimeDelayMillsWhenException=" + pullTimeDelayMillsWhenException +
                ", unitMode=" + unitMode +
                ", unitName='" + unitName + '\'' +
                ", vipChannelEnabled=" + vipChannelEnabled +
                ", useTLS=" + useTLS +
                ", consumerGroup='" + consumerGroup + '\'' +
                ", messageModel=" + messageModel +
                ", consumeFromWhere=" + consumeFromWhere +
                ", consumeTimestamp='" + consumeTimestamp + '\'' +
                ", consumeThreadMin=" + consumeThreadMin +
                ", consumeThreadMax=" + consumeThreadMax +
                ", adjustThreadPoolNumsThreshold=" + adjustThreadPoolNumsThreshold +
                ", consumeConcurrentlyMaxSpan=" + consumeConcurrentlyMaxSpan +
                ", pullThresholdForQueue=" + pullThresholdForQueue +
                ", pullThresholdSizeForQueue=" + pullThresholdSizeForQueue +
                ", pullThresholdForTopic=" + pullThresholdForTopic +
                ", pullThresholdSizeForTopic=" + pullThresholdSizeForTopic +
                ", pullInterval=" + pullInterval +
                ", consumeMessageBatchMaxSize=" + consumeMessageBatchMaxSize +
                ", pullBatchSize=" + pullBatchSize +
                ", postSubscriptionWhenPull=" + postSubscriptionWhenPull +
                ", maxReconsumeTimes=" + maxReconsumeTimes +
                ", suspendCurrentQueueTimeMillis=" + suspendCurrentQueueTimeMillis +
                ", consumeTimeout=" + consumeTimeout +
                ", awaitTerminationMillisWhenShutdown=" + awaitTerminationMillisWhenShutdown +
                '}';
    }

}

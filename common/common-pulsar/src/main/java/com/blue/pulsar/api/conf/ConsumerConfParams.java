package com.blue.pulsar.api.conf;

import org.apache.pulsar.client.api.*;

import java.util.List;


/**
 * consumer params
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public class ConsumerConfParams implements ConsumerConf {

    protected transient List<String> topics;

    protected transient String topicsPattern;

    protected transient String subscriptionName;

    protected Long ackTimeoutMillis;

    protected Boolean enableAckReceipt;

    protected Long ackTimeoutTickTimeMillis;

    protected Long negativeAckRedeliveryDelayMillis;

    protected SubscriptionType subscriptionType;

    protected SubscriptionMode subscriptionMode;

    protected Integer receiverQueueSize;

    protected Long acknowledgementsGroupTimeMillis;

    protected Boolean replicateSubscriptionState;

    protected Integer maxTotalReceiverQueueSizeAcrossPartitions;

    protected String consumerName;

    protected Boolean readCompacted;

    protected Integer patternAutoDiscoveryPeriodMillis;

    protected Integer priorityLevel;

    protected SubscriptionInitialPosition subscriptionInitialPosition;

    protected RegexSubscriptionMode regexSubscriptionMode;

    protected Boolean autoUpdatePartitions;

    protected Integer autoUpdatePartitionsIntervalMillis;

    protected Boolean enableRetry;

    protected Boolean enableBatchIndexAcknowledgment;

    protected Integer maxPendingChunkedMessage;

    protected Boolean autoAckOldestChunkedMessageOnQueueFull;

    protected Long expireTimeOfIncompleteChunkedMessageMillis;

    protected Boolean poolMessages;

    protected Boolean enableEncrypt;

    protected String encryptionKey;

    protected ConsumerCryptoFailureAction consumerCryptoFailureAction;

    protected Boolean enableDeadLetter;

    protected transient String deadLetterTopic;

    protected transient String retryLetterTopic;

    protected Integer maxRedeliverCount;

    protected Boolean startMessageIdInclusive;

    protected Boolean enableBatchReceive;

    protected Integer batchReceiveMaxNumBytes;

    protected Integer batchReceiveMaxNumMessages;

    protected Integer batchReceiveTimeoutMillis;

    protected Integer pollDurationMills;

    protected Integer workingThreads;

    protected Boolean enableNegativeAcknowledge;

    public ConsumerConfParams() {
    }

    public ConsumerConfParams(List<String> topics, String topicsPattern, String subscriptionName, Long ackTimeoutMillis, Boolean enableAckReceipt,
                              Long ackTimeoutTickTimeMillis, Long negativeAckRedeliveryDelayMillis, SubscriptionType subscriptionType, SubscriptionMode subscriptionMode,
                              Integer receiverQueueSize, Long acknowledgementsGroupTimeMillis, Boolean replicateSubscriptionState, Integer maxTotalReceiverQueueSizeAcrossPartitions,
                              String consumerName, Boolean readCompacted, Integer patternAutoDiscoveryPeriodMillis, Integer priorityLevel,
                              SubscriptionInitialPosition subscriptionInitialPosition, RegexSubscriptionMode regexSubscriptionMode, Boolean autoUpdatePartitions,
                              Integer autoUpdatePartitionsIntervalMillis, Boolean enableRetry, Boolean enableBatchIndexAcknowledgment, Integer maxPendingChunkedMessage,
                              Boolean autoAckOldestChunkedMessageOnQueueFull, Long expireTimeOfIncompleteChunkedMessageMillis, Boolean poolMessages, Boolean enableEncrypt,
                              String encryptionKey, ConsumerCryptoFailureAction consumerCryptoFailureAction, Boolean enableDeadLetter, String deadLetterTopic,
                              String retryLetterTopic, Integer maxRedeliverCount, Boolean startMessageIdInclusive, Boolean enableBatchReceive, Integer batchReceiveMaxNumBytes,
                              Integer batchReceiveMaxNumMessages, Integer batchReceiveTimeoutMillis, Integer pollDurationMills, Integer workingThreads, Boolean enableNegativeAcknowledge) {
        this.topics = topics;
        this.topicsPattern = topicsPattern;
        this.subscriptionName = subscriptionName;
        this.ackTimeoutMillis = ackTimeoutMillis;
        this.enableAckReceipt = enableAckReceipt;
        this.ackTimeoutTickTimeMillis = ackTimeoutTickTimeMillis;
        this.negativeAckRedeliveryDelayMillis = negativeAckRedeliveryDelayMillis;
        this.subscriptionType = subscriptionType;
        this.subscriptionMode = subscriptionMode;
        this.receiverQueueSize = receiverQueueSize;
        this.acknowledgementsGroupTimeMillis = acknowledgementsGroupTimeMillis;
        this.replicateSubscriptionState = replicateSubscriptionState;
        this.maxTotalReceiverQueueSizeAcrossPartitions = maxTotalReceiverQueueSizeAcrossPartitions;
        this.consumerName = consumerName;
        this.readCompacted = readCompacted;
        this.patternAutoDiscoveryPeriodMillis = patternAutoDiscoveryPeriodMillis;
        this.priorityLevel = priorityLevel;
        this.subscriptionInitialPosition = subscriptionInitialPosition;
        this.regexSubscriptionMode = regexSubscriptionMode;
        this.autoUpdatePartitions = autoUpdatePartitions;
        this.autoUpdatePartitionsIntervalMillis = autoUpdatePartitionsIntervalMillis;
        this.enableRetry = enableRetry;
        this.enableBatchIndexAcknowledgment = enableBatchIndexAcknowledgment;
        this.maxPendingChunkedMessage = maxPendingChunkedMessage;
        this.autoAckOldestChunkedMessageOnQueueFull = autoAckOldestChunkedMessageOnQueueFull;
        this.expireTimeOfIncompleteChunkedMessageMillis = expireTimeOfIncompleteChunkedMessageMillis;
        this.poolMessages = poolMessages;
        this.enableEncrypt = enableEncrypt;
        this.encryptionKey = encryptionKey;
        this.consumerCryptoFailureAction = consumerCryptoFailureAction;
        this.enableDeadLetter = enableDeadLetter;
        this.deadLetterTopic = deadLetterTopic;
        this.retryLetterTopic = retryLetterTopic;
        this.maxRedeliverCount = maxRedeliverCount;
        this.startMessageIdInclusive = startMessageIdInclusive;
        this.enableBatchReceive = enableBatchReceive;
        this.batchReceiveMaxNumBytes = batchReceiveMaxNumBytes;
        this.batchReceiveMaxNumMessages = batchReceiveMaxNumMessages;
        this.batchReceiveTimeoutMillis = batchReceiveTimeoutMillis;
        this.pollDurationMills = pollDurationMills;
        this.workingThreads = workingThreads;
        this.enableNegativeAcknowledge = enableNegativeAcknowledge;
    }

    @Override
    public List<String> getTopics() {
        return topics;
    }

    @Override
    public String getTopicsPattern() {
        return topicsPattern;
    }

    @Override
    public String getSubscriptionName() {
        return subscriptionName;
    }

    @Override
    public Long getAckTimeoutMillis() {
        return ackTimeoutMillis;
    }

    @Override
    public Boolean getEnableAckReceipt() {
        return enableAckReceipt;
    }

    @Override
    public Long getAckTimeoutTickTimeMillis() {
        return ackTimeoutTickTimeMillis;
    }

    @Override
    public Long getNegativeAckRedeliveryDelayMillis() {
        return negativeAckRedeliveryDelayMillis;
    }

    @Override
    public SubscriptionType getSubscriptionType() {
        return subscriptionType;
    }

    @Override
    public SubscriptionMode getSubscriptionMode() {
        return subscriptionMode;
    }

    @Override
    public Integer getReceiverQueueSize() {
        return receiverQueueSize;
    }

    @Override
    public Long getAcknowledgementsGroupTimeMillis() {
        return acknowledgementsGroupTimeMillis;
    }

    @Override
    public Boolean getReplicateSubscriptionState() {
        return replicateSubscriptionState;
    }

    @Override
    public Integer getMaxTotalReceiverQueueSizeAcrossPartitions() {
        return maxTotalReceiverQueueSizeAcrossPartitions;
    }

    @Override
    public String getConsumerName() {
        return consumerName;
    }

    @Override
    public Boolean getReadCompacted() {
        return readCompacted;
    }

    @Override
    public Integer getPatternAutoDiscoveryPeriodMillis() {
        return patternAutoDiscoveryPeriodMillis;
    }

    @Override
    public Integer getPriorityLevel() {
        return priorityLevel;
    }

    @Override
    public SubscriptionInitialPosition getSubscriptionInitialPosition() {
        return subscriptionInitialPosition;
    }

    @Override
    public RegexSubscriptionMode getRegexSubscriptionMode() {
        return regexSubscriptionMode;
    }

    @Override
    public Boolean getAutoUpdatePartitions() {
        return autoUpdatePartitions;
    }

    @Override
    public Integer getAutoUpdatePartitionsIntervalMillis() {
        return autoUpdatePartitionsIntervalMillis;
    }

    @Override
    public Boolean getEnableRetry() {
        return enableRetry;
    }

    @Override
    public Boolean getEnableBatchIndexAcknowledgment() {
        return enableBatchIndexAcknowledgment;
    }

    @Override
    public Integer getMaxPendingChunkedMessage() {
        return maxPendingChunkedMessage;
    }

    @Override
    public Boolean getAutoAckOldestChunkedMessageOnQueueFull() {
        return autoAckOldestChunkedMessageOnQueueFull;
    }

    @Override
    public Long getExpireTimeOfIncompleteChunkedMessageMillis() {
        return expireTimeOfIncompleteChunkedMessageMillis;
    }

    @Override
    public Boolean getPoolMessages() {
        return poolMessages;
    }

    @Override
    public Boolean getEnableEncrypt() {
        return enableEncrypt;
    }

    @Override
    public String getEncryptionKey() {
        return encryptionKey;
    }

    @Override
    public ConsumerCryptoFailureAction getConsumerCryptoFailureAction() {
        return consumerCryptoFailureAction;
    }

    @Override
    public Boolean getEnableDeadLetter() {
        return enableDeadLetter;
    }

    @Override
    public String getDeadLetterTopic() {
        return deadLetterTopic;
    }

    @Override
    public String getRetryLetterTopic() {
        return retryLetterTopic;
    }

    @Override
    public Integer getMaxRedeliverCount() {
        return maxRedeliverCount;
    }

    @Override
    public Boolean getStartMessageIdInclusive() {
        return startMessageIdInclusive;
    }

    @Override
    public Boolean getEnableBatchReceive() {
        return enableBatchReceive;
    }

    @Override
    public Integer getBatchReceiveMaxNumBytes() {
        return batchReceiveMaxNumBytes;
    }

    @Override
    public Integer getBatchReceiveMaxNumMessages() {
        return batchReceiveMaxNumMessages;
    }

    @Override
    public Integer getBatchReceiveTimeoutMillis() {
        return batchReceiveTimeoutMillis;
    }

    @Override
    public Integer getPollDurationMills() {
        return pollDurationMills;
    }

    @Override
    public Integer getWorkingThreads() {
        return workingThreads;
    }

    @Override
    public Boolean getEnableNegativeAcknowledge() {
        return enableNegativeAcknowledge;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public void setTopicsPattern(String topicsPattern) {
        this.topicsPattern = topicsPattern;
    }

    public void setSubscriptionName(String subscriptionName) {
        this.subscriptionName = subscriptionName;
    }

    public void setAckTimeoutMillis(Long ackTimeoutMillis) {
        this.ackTimeoutMillis = ackTimeoutMillis;
    }

    public void setEnableAckReceipt(Boolean enableAckReceipt) {
        this.enableAckReceipt = enableAckReceipt;
    }

    public void setAckTimeoutTickTimeMillis(Long ackTimeoutTickTimeMillis) {
        this.ackTimeoutTickTimeMillis = ackTimeoutTickTimeMillis;
    }

    public void setNegativeAckRedeliveryDelayMillis(Long negativeAckRedeliveryDelayMillis) {
        this.negativeAckRedeliveryDelayMillis = negativeAckRedeliveryDelayMillis;
    }

    public void setSubscriptionType(SubscriptionType subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public void setSubscriptionMode(SubscriptionMode subscriptionMode) {
        this.subscriptionMode = subscriptionMode;
    }

    public void setReceiverQueueSize(Integer receiverQueueSize) {
        this.receiverQueueSize = receiverQueueSize;
    }

    public void setAcknowledgementsGroupTimeMillis(Long acknowledgementsGroupTimeMillis) {
        this.acknowledgementsGroupTimeMillis = acknowledgementsGroupTimeMillis;
    }

    public void setReplicateSubscriptionState(Boolean replicateSubscriptionState) {
        this.replicateSubscriptionState = replicateSubscriptionState;
    }

    public void setMaxTotalReceiverQueueSizeAcrossPartitions(Integer maxTotalReceiverQueueSizeAcrossPartitions) {
        this.maxTotalReceiverQueueSizeAcrossPartitions = maxTotalReceiverQueueSizeAcrossPartitions;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public void setReadCompacted(Boolean readCompacted) {
        this.readCompacted = readCompacted;
    }

    public void setPatternAutoDiscoveryPeriodMillis(Integer patternAutoDiscoveryPeriodMillis) {
        this.patternAutoDiscoveryPeriodMillis = patternAutoDiscoveryPeriodMillis;
    }

    public void setPriorityLevel(Integer priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public void setSubscriptionInitialPosition(SubscriptionInitialPosition subscriptionInitialPosition) {
        this.subscriptionInitialPosition = subscriptionInitialPosition;
    }

    public void setRegexSubscriptionMode(RegexSubscriptionMode regexSubscriptionMode) {
        this.regexSubscriptionMode = regexSubscriptionMode;
    }

    public void setAutoUpdatePartitions(Boolean autoUpdatePartitions) {
        this.autoUpdatePartitions = autoUpdatePartitions;
    }

    public void setAutoUpdatePartitionsIntervalMillis(Integer autoUpdatePartitionsIntervalMillis) {
        this.autoUpdatePartitionsIntervalMillis = autoUpdatePartitionsIntervalMillis;
    }

    public void setEnableRetry(Boolean enableRetry) {
        this.enableRetry = enableRetry;
    }

    public void setEnableBatchIndexAcknowledgment(Boolean enableBatchIndexAcknowledgment) {
        this.enableBatchIndexAcknowledgment = enableBatchIndexAcknowledgment;
    }

    public void setMaxPendingChunkedMessage(Integer maxPendingChunkedMessage) {
        this.maxPendingChunkedMessage = maxPendingChunkedMessage;
    }

    public void setAutoAckOldestChunkedMessageOnQueueFull(Boolean autoAckOldestChunkedMessageOnQueueFull) {
        this.autoAckOldestChunkedMessageOnQueueFull = autoAckOldestChunkedMessageOnQueueFull;
    }

    public void setExpireTimeOfIncompleteChunkedMessageMillis(Long expireTimeOfIncompleteChunkedMessageMillis) {
        this.expireTimeOfIncompleteChunkedMessageMillis = expireTimeOfIncompleteChunkedMessageMillis;
    }

    public void setPoolMessages(Boolean poolMessages) {
        this.poolMessages = poolMessages;
    }

    public void setEnableEncrypt(Boolean enableEncrypt) {
        this.enableEncrypt = enableEncrypt;
    }

    public void setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    public void setConsumerCryptoFailureAction(ConsumerCryptoFailureAction consumerCryptoFailureAction) {
        this.consumerCryptoFailureAction = consumerCryptoFailureAction;
    }

    public void setEnableDeadLetter(Boolean enableDeadLetter) {
        this.enableDeadLetter = enableDeadLetter;
    }

    public void setDeadLetterTopic(String deadLetterTopic) {
        this.deadLetterTopic = deadLetterTopic;
    }

    public void setRetryLetterTopic(String retryLetterTopic) {
        this.retryLetterTopic = retryLetterTopic;
    }

    public void setMaxRedeliverCount(Integer maxRedeliverCount) {
        this.maxRedeliverCount = maxRedeliverCount;
    }

    public void setStartMessageIdInclusive(Boolean startMessageIdInclusive) {
        this.startMessageIdInclusive = startMessageIdInclusive;
    }

    public void setEnableBatchReceive(Boolean enableBatchReceive) {
        this.enableBatchReceive = enableBatchReceive;
    }

    public void setBatchReceiveMaxNumBytes(Integer batchReceiveMaxNumBytes) {
        this.batchReceiveMaxNumBytes = batchReceiveMaxNumBytes;
    }

    public void setBatchReceiveMaxNumMessages(Integer batchReceiveMaxNumMessages) {
        this.batchReceiveMaxNumMessages = batchReceiveMaxNumMessages;
    }

    public void setBatchReceiveTimeoutMillis(Integer batchReceiveTimeoutMillis) {
        this.batchReceiveTimeoutMillis = batchReceiveTimeoutMillis;
    }

    public void setPollDurationMills(Integer pollDurationMills) {
        this.pollDurationMills = pollDurationMills;
    }

    public void setWorkingThreads(Integer workingThreads) {
        this.workingThreads = workingThreads;
    }

    public void setEnableNegativeAcknowledge(Boolean enableNegativeAcknowledge) {
        this.enableNegativeAcknowledge = enableNegativeAcknowledge;
    }

    @Override
    public String toString() {
        return "ConsumerConfParams{" +
                "topics=" + topics +
                ", topicsPattern='" + topicsPattern + '\'' +
                ", subscriptionName='" + subscriptionName + '\'' +
                ", ackTimeoutMillis=" + ackTimeoutMillis +
                ", enableAckReceipt=" + enableAckReceipt +
                ", ackTimeoutTickTimeMillis=" + ackTimeoutTickTimeMillis +
                ", negativeAckRedeliveryDelayMillis=" + negativeAckRedeliveryDelayMillis +
                ", subscriptionType=" + subscriptionType +
                ", subscriptionMode=" + subscriptionMode +
                ", receiverQueueSize=" + receiverQueueSize +
                ", acknowledgementsGroupTimeMillis=" + acknowledgementsGroupTimeMillis +
                ", replicateSubscriptionState=" + replicateSubscriptionState +
                ", maxTotalReceiverQueueSizeAcrossPartitions=" + maxTotalReceiverQueueSizeAcrossPartitions +
                ", consumerName='" + consumerName + '\'' +
                ", readCompacted=" + readCompacted +
                ", patternAutoDiscoveryPeriodMillis=" + patternAutoDiscoveryPeriodMillis +
                ", priorityLevel=" + priorityLevel +
                ", subscriptionInitialPosition=" + subscriptionInitialPosition +
                ", regexSubscriptionMode=" + regexSubscriptionMode +
                ", autoUpdatePartitions=" + autoUpdatePartitions +
                ", autoUpdatePartitionsIntervalMillis=" + autoUpdatePartitionsIntervalMillis +
                ", enableRetry=" + enableRetry +
                ", enableBatchIndexAcknowledgment=" + enableBatchIndexAcknowledgment +
                ", maxPendingChunkedMessage=" + maxPendingChunkedMessage +
                ", autoAckOldestChunkedMessageOnQueueFull=" + autoAckOldestChunkedMessageOnQueueFull +
                ", expireTimeOfIncompleteChunkedMessageMillis=" + expireTimeOfIncompleteChunkedMessageMillis +
                ", poolMessages=" + poolMessages +
                ", enableEncrypt=" + enableEncrypt +
                ", encryptionKey='" + encryptionKey + '\'' +
                ", consumerCryptoFailureAction=" + consumerCryptoFailureAction +
                ", enableDeadLetter=" + enableDeadLetter +
                ", deadLetterTopic='" + deadLetterTopic + '\'' +
                ", retryLetterTopic='" + retryLetterTopic + '\'' +
                ", maxRedeliverCount=" + maxRedeliverCount +
                ", startMessageIdInclusive=" + startMessageIdInclusive +
                ", enableBatchReceive=" + enableBatchReceive +
                ", batchReceiveMaxNumBytes=" + batchReceiveMaxNumBytes +
                ", batchReceiveMaxNumMessages=" + batchReceiveMaxNumMessages +
                ", batchReceiveTimeoutMillis=" + batchReceiveTimeoutMillis +
                ", pollDurationMills=" + pollDurationMills +
                ", workingThreads=" + workingThreads +
                ", enableNegativeAcknowledge=" + enableNegativeAcknowledge +
                '}';
    }

}

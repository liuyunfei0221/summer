package com.blue.pulsar.api.conf;

import org.apache.pulsar.client.api.*;

import java.util.List;


/**
 * pulsar consumer conf
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public interface ConsumerConf extends ClientConf {

    List<String> getTopics();

    String getTopicsPattern();

    String getSubscriptionName();

    Long getAckTimeoutMillis();

    Boolean getEnableAckReceipt();

    Long getAckTimeoutTickTimeMillis();

    Long getNegativeAckRedeliveryDelayMillis();

    SubscriptionType getSubscriptionType();

    SubscriptionMode getSubscriptionMode();

    Integer getReceiverQueueSize();

    Long getAcknowledgementsGroupTimeMillis();

    Boolean getReplicateSubscriptionState();

    Integer getMaxTotalReceiverQueueSizeAcrossPartitions();

    String getConsumerName();

    Boolean getReadCompacted();

    Integer getPatternAutoDiscoveryPeriodMillis();

    Integer getPriorityLevel();

    SubscriptionInitialPosition getSubscriptionInitialPosition();

    RegexSubscriptionMode getRegexSubscriptionMode();

    Boolean getAutoUpdatePartitions();

    Integer getAutoUpdatePartitionsIntervalMillis();

    Boolean getEnableRetry();

    Boolean getEnableBatchIndexAcknowledgment();

    Integer getMaxPendingChunkedMessage();

    Boolean getAutoAckOldestChunkedMessageOnQueueFull();

    Long getExpireTimeOfIncompleteChunkedMessageMillis();

    Boolean getPoolMessages();

    Boolean getEnableEncrypt();

    String getEncryptionKey();

    ConsumerCryptoFailureAction getConsumerCryptoFailureAction();

    Boolean getEnableDeadLetter();

    String getDeadLetterTopic();

    String getRetryLetterTopic();

    Integer getMaxRedeliverCount();

    Boolean getStartMessageIdInclusive();

    Boolean getEnableBatchReceive();

    Integer getBatchReceiveMaxNumMessages();

    Integer getBatchReceiveMaxNumBytes();

    Integer getBatchReceiveTimeoutMillis();

    Integer getPollDurationMills();

    Integer getWorkingThreads();

    Boolean getEnableNegativeAcknowledge();

}

package com.blue.pulsar.api.conf;

import org.apache.pulsar.client.api.*;

import java.util.List;

/**
 * pulsar producer conf
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public interface ProducerConf extends ClientConf {

    String getTopic();

    String getProducerName();

    ProducerAccessMode getAccessMode();

    Integer getSendTimeoutMillis();

    Integer getMaxPendingMessages();

    Boolean getBlockIfQueueFull();

    MessageRoutingMode getMessageRoutingMode();

    CompressionType getCompressionType();

    Boolean getEnableBatching();

    Boolean getEnableChunking();

    Long getBatchingMaxPublishDelayMillis();

    Integer getRoundRobinRouterBatchingPartitionSwitchFrequency();

    Integer getBatchingMaxMessages();

    Integer getBatchingMaxBytes();

    Long getInitialSequenceId();

    Boolean getAutoUpdatePartitions();

    Integer getAutoUpdatePartitionsIntervalMillis();

    Boolean getEnableMultiSchema();

    List<HashingScheme> getHashingSchemes();

    Boolean getEnableEncrypt();

    String getEncryptionKey();

    ProducerCryptoFailureAction getProducerCryptoFailureAction();

}

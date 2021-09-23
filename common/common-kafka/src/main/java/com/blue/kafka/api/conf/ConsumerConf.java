package com.blue.kafka.api.conf;

/**
 * kafka单消费实例配置信息
 *
 * @author DarkBlue
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
public interface ConsumerConf {

    String getBootstrap();

    String getUsername();

    String getPassword();

    String getTopic();

    String getGroupId();

    String getGroupSequence();

    Boolean getEnableAutoCommit();

    String getAutoOffsetReset();

    Integer getAutoCommitIntervalMs();

    Integer getPollDurationMills();

    Integer getWorkingThreads();

}

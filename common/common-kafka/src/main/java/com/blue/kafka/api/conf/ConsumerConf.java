package com.blue.kafka.api.conf;

/**
 * kafka consumer conf
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

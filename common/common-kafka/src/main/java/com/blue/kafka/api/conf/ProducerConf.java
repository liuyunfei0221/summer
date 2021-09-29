package com.blue.kafka.api.conf;

/**
 * kafka producer conf
 *
 * @author DarkBlue
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
public interface ProducerConf {

    String getBootstrap();

    String getUsername();

    String getPassword();

    String getTopic();

    String getGroupId();

    String getAcks();

    Integer getRetries();

    Integer getBatchSize();

    Integer getLingerMs();

    Integer getBufferMemory();

}

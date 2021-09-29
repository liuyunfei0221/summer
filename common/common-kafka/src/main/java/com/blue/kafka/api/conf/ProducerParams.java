package com.blue.kafka.api.conf;

/**
 * kafka producer params
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class ProducerParams implements ProducerConf {

    private String bootstrap;

    private String username;

    private String password;

    private String topic;

    private String groupId;

    private String acks;

    private Integer retries;

    private Integer batchSize;

    private Integer lingerMs;

    private Integer bufferMemory;

    public ProducerParams() {
    }

    public ProducerParams(String bootstrap, String username, String password,
                          String topic, String groupId, String acks, Integer retries,
                          Integer batchSize, Integer lingerMs, Integer bufferMemory) {
        this.bootstrap = bootstrap;
        this.username = username;
        this.password = password;
        this.topic = topic;
        this.groupId = groupId;
        this.acks = acks;
        this.retries = retries;
        this.batchSize = batchSize;
        this.lingerMs = lingerMs;
        this.bufferMemory = bufferMemory;
    }

    @Override
    public String getBootstrap() {
        return bootstrap;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getTopic() {
        return topic;
    }

    @Override
    public String getGroupId() {
        return groupId;
    }

    @Override
    public String getAcks() {
        return acks;
    }

    @Override
    public Integer getRetries() {
        return retries;
    }

    @Override
    public Integer getBatchSize() {
        return batchSize;
    }

    @Override
    public Integer getLingerMs() {
        return lingerMs;
    }

    @Override
    public Integer getBufferMemory() {
        return bufferMemory;
    }

    public void setBootstrap(String bootstrap) {
        this.bootstrap = bootstrap;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setAcks(String acks) {
        this.acks = acks;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
    }

    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }

    public void setLingerMs(Integer lingerMs) {
        this.lingerMs = lingerMs;
    }

    public void setBufferMemory(Integer bufferMemory) {
        this.bufferMemory = bufferMemory;
    }

    @Override
    public String toString() {
        return "ProducerParams{" +
                "bootstrap='" + bootstrap + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", topic='" + topic + '\'' +
                ", groupId='" + groupId + '\'' +
                ", acks='" + acks + '\'' +
                ", retries=" + retries +
                ", batchSize=" + batchSize +
                ", lingerMs=" + lingerMs +
                ", bufferMemory=" + bufferMemory +
                '}';
    }

}

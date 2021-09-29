package com.blue.kafka.api.conf;

/**
 * kafka consumer params
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public class ConsumerParams implements ConsumerConf {

    protected String bootstrap;

    protected String username;

    protected String password;

    protected String topic;

    protected String groupId;

    protected String groupSequence;

    protected Boolean enableAutoCommit;

    protected String autoOffsetReset;

    protected Integer autoCommitIntervalMs;

    protected Integer pollDurationMills;

    protected Integer workingThreads;

    public ConsumerParams() {
    }

    public ConsumerParams(String bootstrap, String username, String password,
                          String topic, String groupId, String groupSequence,
                          Boolean enableAutoCommit, String autoOffsetReset,
                          Integer autoCommitIntervalMs, Integer pollDurationMills,
                          Integer workingThreads) {
        this.bootstrap = bootstrap;
        this.username = username;
        this.password = password;
        this.topic = topic;
        this.groupId = groupId;
        this.groupSequence = groupSequence;
        this.enableAutoCommit = enableAutoCommit;
        this.autoOffsetReset = autoOffsetReset;
        this.autoCommitIntervalMs = autoCommitIntervalMs;
        this.pollDurationMills = pollDurationMills;
        this.workingThreads = workingThreads;
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
    public String getGroupSequence() {
        return groupSequence;
    }

    @Override
    public Boolean getEnableAutoCommit() {
        return enableAutoCommit;
    }

    @Override
    public String getAutoOffsetReset() {
        return autoOffsetReset;
    }

    @Override
    public Integer getAutoCommitIntervalMs() {
        return autoCommitIntervalMs;
    }

    @Override
    public Integer getPollDurationMills() {
        return pollDurationMills;
    }

    @Override
    public Integer getWorkingThreads() {
        return workingThreads;
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

    public void setGroupSequence(String groupSequence) {
        this.groupSequence = groupSequence;
    }

    public void setEnableAutoCommit(Boolean enableAutoCommit) {
        this.enableAutoCommit = enableAutoCommit;
    }

    public void setAutoOffsetReset(String autoOffsetReset) {
        this.autoOffsetReset = autoOffsetReset;
    }

    public void setAutoCommitIntervalMs(Integer autoCommitIntervalMs) {
        this.autoCommitIntervalMs = autoCommitIntervalMs;
    }

    public void setPollDurationMills(Integer pollDurationMills) {
        this.pollDurationMills = pollDurationMills;
    }

    public void setWorkingThreads(Integer workingThreads) {
        this.workingThreads = workingThreads;
    }

    @Override
    public String toString() {
        return "ConsumerParams{" +
                "bootstrap='" + bootstrap + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", topic='" + topic + '\'' +
                ", groupId='" + groupId + '\'' +
                ", groupSequence='" + groupSequence + '\'' +
                ", enableAutoCommit=" + enableAutoCommit +
                ", autoOffsetReset='" + autoOffsetReset + '\'' +
                ", autoCommitIntervalMs=" + autoCommitIntervalMs +
                ", pollDurationMills=" + pollDurationMills +
                ", workingThreads=" + workingThreads +
                '}';
    }

}

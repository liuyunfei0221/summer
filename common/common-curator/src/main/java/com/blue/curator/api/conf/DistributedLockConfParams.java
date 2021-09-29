package com.blue.curator.api.conf;

/**
 * zk lock params
 *
 * @author liuyunfei
 * @date 2021/9/10
 * @apiNote
 */
@SuppressWarnings("unused")
public class DistributedLockConfParams implements DistributedLockConf {

    protected String connectString;

    protected String authSchema;

    protected String auth;

    protected Integer connectionTimeoutMs;

    protected Integer sessionTimeoutMs;

    protected Integer maxCloseWaitMs;

    protected Integer retryBaseSleepTimeMs;

    protected Integer retryMaxSleepTimeMs;

    protected Integer retryMaxRetries;

    protected String namespace;

    protected Boolean canBeReadOnly;

    public DistributedLockConfParams() {
    }

    @Override
    public String getConnectString() {
        return connectString;
    }

    @Override
    public String getAuthSchema() {
        return authSchema;
    }

    @Override
    public String getAuth() {
        return auth;
    }

    @Override
    public Integer getConnectionTimeoutMs() {
        return connectionTimeoutMs;
    }

    @Override
    public Integer getSessionTimeoutMs() {
        return sessionTimeoutMs;
    }

    @Override
    public Integer getMaxCloseWaitMs() {
        return maxCloseWaitMs;
    }

    @Override
    public Integer getRetryBaseSleepTimeMs() {
        return retryBaseSleepTimeMs;
    }

    @Override
    public Integer getRetryMaxSleepTimeMs() {
        return retryMaxSleepTimeMs;
    }

    @Override
    public Integer getRetryMaxRetries() {
        return retryMaxRetries;
    }

    @Override
    public String getNamespace() {
        return namespace;
    }

    @Override
    public Boolean getCanBeReadOnly() {
        return canBeReadOnly;
    }

    public void setConnectString(String connectString) {
        this.connectString = connectString;
    }

    public void setAuthSchema(String authSchema) {
        this.authSchema = authSchema;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public void setConnectionTimeoutMs(Integer connectionTimeoutMs) {
        this.connectionTimeoutMs = connectionTimeoutMs;
    }

    public void setSessionTimeoutMs(Integer sessionTimeoutMs) {
        this.sessionTimeoutMs = sessionTimeoutMs;
    }

    public void setMaxCloseWaitMs(Integer maxCloseWaitMs) {
        this.maxCloseWaitMs = maxCloseWaitMs;
    }

    public void setRetryBaseSleepTimeMs(Integer retryBaseSleepTimeMs) {
        this.retryBaseSleepTimeMs = retryBaseSleepTimeMs;
    }

    public void setRetryMaxSleepTimeMs(Integer retryMaxSleepTimeMs) {
        this.retryMaxSleepTimeMs = retryMaxSleepTimeMs;
    }

    public void setRetryMaxRetries(Integer retryMaxRetries) {
        this.retryMaxRetries = retryMaxRetries;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public void setCanBeReadOnly(Boolean canBeReadOnly) {
        this.canBeReadOnly = canBeReadOnly;
    }

    @Override
    public String toString() {
        return "DistributedLockConfParams{" +
                "connectString='" + connectString + '\'' +
                ", authSchema='" + authSchema + '\'' +
                ", auth='" + auth + '\'' +
                ", connectionTimeoutMs=" + connectionTimeoutMs +
                ", sessionTimeoutMs=" + sessionTimeoutMs +
                ", maxCloseWaitMs=" + maxCloseWaitMs +
                ", retryBaseSleepTimeMs=" + retryBaseSleepTimeMs +
                ", retryMaxSleepTimeMs=" + retryMaxSleepTimeMs +
                ", retryMaxRetries=" + retryMaxRetries +
                ", namespace='" + namespace + '\'' +
                ", canBeReadOnly=" + canBeReadOnly +
                '}';
    }

}

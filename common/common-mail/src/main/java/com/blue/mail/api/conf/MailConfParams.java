package com.blue.mail.api.conf;

import org.simplejavamail.api.mailer.config.LoadBalancingStrategy;
import org.simplejavamail.api.mailer.config.TransportStrategy;

import java.util.Map;

/**
 * @author liuyunfei
 * @date 2022/1/4
 * @apiNote
 */
@SuppressWarnings({"unused", "AlibabaLowerCamelCaseVariableNaming"})
public abstract class MailConfParams implements MailConf {

    protected String smtpServerHost;

    protected Integer smtpServerPort;

    protected String smtpUsername;

    protected String smtpPassword;

    protected Integer corePoolSize;

    protected Integer maximumPoolSize;

    protected Long keepAliveSeconds;

    protected Integer blockingQueueCapacity;

    protected String threadNamePre;

    protected Integer connectionPoolCoreSize;

    protected Integer connectionPoolMaxSize;

    protected Integer connectionPoolClaimTimeoutMillis;

    protected Integer connectionPoolExpireAfterMillis;

    protected Integer sessionTimeout;

    protected LoadBalancingStrategy connectionPoolLoadBalancingStrategy;

    protected TransportStrategy transportStrategy;

    protected Boolean withDKIM;

    protected String domainKeyFile;

    protected String domain;

    protected String selector;

    protected Boolean debugLogging;

    protected Map<String, String> props;

    public MailConfParams() {
    }

    @Override
    public String getSmtpServerHost() {
        return smtpServerHost;
    }

    @Override
    public Integer getSmtpServerPort() {
        return smtpServerPort;
    }

    @Override
    public String getSmtpUsername() {
        return smtpUsername;
    }

    @Override
    public String getSmtpPassword() {
        return smtpPassword;
    }

    @Override
    public Integer getCorePoolSize() {
        return corePoolSize;
    }

    @Override
    public Integer getMaximumPoolSize() {
        return maximumPoolSize;
    }

    @Override
    public Long getKeepAliveSeconds() {
        return keepAliveSeconds;
    }

    @Override
    public Integer getBlockingQueueCapacity() {
        return blockingQueueCapacity;
    }

    @Override
    public String getThreadNamePre() {
        return threadNamePre;
    }

    @Override
    public Integer getConnectionPoolCoreSize() {
        return connectionPoolCoreSize;
    }

    @Override
    public Integer getConnectionPoolMaxSize() {
        return connectionPoolMaxSize;
    }

    @Override
    public Integer getConnectionPoolClaimTimeoutMillis() {
        return connectionPoolClaimTimeoutMillis;
    }

    @Override
    public Integer getConnectionPoolExpireAfterMillis() {
        return connectionPoolExpireAfterMillis;
    }

    @Override
    public Integer getSessionTimeout() {
        return sessionTimeout;
    }

    @Override
    public LoadBalancingStrategy getConnectionPoolLoadBalancingStrategy() {
        return connectionPoolLoadBalancingStrategy;
    }

    @Override
    public TransportStrategy getTransportStrategy() {
        return transportStrategy;
    }

    @Override
    public Boolean getWithDKIM() {
        return withDKIM;
    }

    @Override
    public String getDomainKeyFile() {
        return domainKeyFile;
    }

    @Override
    public String getDomain() {
        return domain;
    }

    @Override
    public String getSelector() {
        return selector;
    }

    @Override
    public Boolean getDebugLogging() {
        return debugLogging;
    }

    @Override
    public Map<String, String> getProps() {
        return props;
    }

    public void setSmtpServerHost(String smtpServerHost) {
        this.smtpServerHost = smtpServerHost;
    }

    public void setSmtpServerPort(Integer smtpServerPort) {
        this.smtpServerPort = smtpServerPort;
    }

    public void setSmtpUsername(String smtpUsername) {
        this.smtpUsername = smtpUsername;
    }

    public void setSmtpPassword(String smtpPassword) {
        this.smtpPassword = smtpPassword;
    }

    public void setCorePoolSize(Integer corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public void setMaximumPoolSize(Integer maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public void setKeepAliveSeconds(Long keepAliveSeconds) {
        this.keepAliveSeconds = keepAliveSeconds;
    }

    public void setBlockingQueueCapacity(Integer blockingQueueCapacity) {
        this.blockingQueueCapacity = blockingQueueCapacity;
    }

    public void setThreadNamePre(String threadNamePre) {
        this.threadNamePre = threadNamePre;
    }

    public void setConnectionPoolCoreSize(Integer connectionPoolCoreSize) {
        this.connectionPoolCoreSize = connectionPoolCoreSize;
    }

    public void setConnectionPoolMaxSize(Integer connectionPoolMaxSize) {
        this.connectionPoolMaxSize = connectionPoolMaxSize;
    }

    public void setConnectionPoolClaimTimeoutMillis(Integer connectionPoolClaimTimeoutMillis) {
        this.connectionPoolClaimTimeoutMillis = connectionPoolClaimTimeoutMillis;
    }

    public void setConnectionPoolExpireAfterMillis(Integer connectionPoolExpireAfterMillis) {
        this.connectionPoolExpireAfterMillis = connectionPoolExpireAfterMillis;
    }

    public void setSessionTimeout(Integer sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public void setConnectionPoolLoadBalancingStrategy(LoadBalancingStrategy connectionPoolLoadBalancingStrategy) {
        this.connectionPoolLoadBalancingStrategy = connectionPoolLoadBalancingStrategy;
    }

    public void setTransportStrategy(TransportStrategy transportStrategy) {
        this.transportStrategy = transportStrategy;
    }

    public void setWithDKIM(Boolean withDKIM) {
        this.withDKIM = withDKIM;
    }

    public void setDomainKeyFile(String domainKeyFile) {
        this.domainKeyFile = domainKeyFile;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public void setDebugLogging(Boolean debugLogging) {
        this.debugLogging = debugLogging;
    }

    public void setProps(Map<String, String> props) {
        this.props = props;
    }

    @Override
    public String toString() {
        return "MailConfParams{" +
                "smtpServerHost='" + smtpServerHost + '\'' +
                ", smtpServerPort=" + smtpServerPort +
                ", smtpUsername='" + smtpUsername + '\'' +
                ", smtpPassword='" + smtpPassword + '\'' +
                ", corePoolSize=" + corePoolSize +
                ", maximumPoolSize=" + maximumPoolSize +
                ", keepAliveSeconds=" + keepAliveSeconds +
                ", blockingQueueCapacity=" + blockingQueueCapacity +
                ", threadNamePre='" + threadNamePre + '\'' +
                ", connectionPoolCoreSize=" + connectionPoolCoreSize +
                ", connectionPoolMaxSize=" + connectionPoolMaxSize +
                ", connectionPoolClaimTimeoutMillis=" + connectionPoolClaimTimeoutMillis +
                ", connectionPoolExpireAfterMillis=" + connectionPoolExpireAfterMillis +
                ", sessionTimeout=" + sessionTimeout +
                ", connectionPoolLoadBalancingStrategy=" + connectionPoolLoadBalancingStrategy +
                ", transportStrategy=" + transportStrategy +
                ", withDKIM=" + withDKIM +
                ", domainKeyFile='" + domainKeyFile + '\'' +
                ", domain='" + domain + '\'' +
                ", selector='" + selector + '\'' +
                ", debugLogging=" + debugLogging +
                ", props=" + props +
                '}';
    }

}

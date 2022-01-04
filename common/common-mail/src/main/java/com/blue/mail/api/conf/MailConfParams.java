package com.blue.mail.api.conf;

import com.sanctionco.jmail.EmailValidator;

import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * @author liuyunfei
 * @date 2022/1/4
 * @apiNote
 */
@SuppressWarnings("unused")
public class MailConfParams implements MailConf {

    protected String smtpServerHost;

    protected Integer smtpServerPort;

    protected Integer smtpUsername;

    protected Integer smtpPassword;

    protected Boolean clearEmailValidator;

    protected EmailValidator emailValidator;

    protected ExecutorService executorService;

    protected Integer connectionPoolCoreSize;

    protected Integer connectionPoolMaxSize;

    protected Integer connectionPoolExpireAfterMillis;

    protected Integer sessionTimeout;

    protected String connectionPoolLoadBalancingStrategy;

    protected String transportStrategy;

    protected Boolean async;

    protected String domainKeyFile;

    protected String domain;

    protected String selector;

    protected Boolean debugLogging;

    protected Map<String, Object> properties;

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
    public Integer getSmtpUsername() {
        return smtpUsername;
    }

    @Override
    public Integer getSmtpPassword() {
        return smtpPassword;
    }

    @Override
    public Boolean getClearEmailValidator() {
        return clearEmailValidator;
    }

    @Override
    public EmailValidator getEmailValidator() {
        return emailValidator;
    }

    @Override
    public ExecutorService getExecutorService() {
        return executorService;
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
    public Integer getConnectionPoolExpireAfterMillis() {
        return connectionPoolExpireAfterMillis;
    }

    @Override
    public Integer getSessionTimeout() {
        return sessionTimeout;
    }

    @Override
    public String getConnectionPoolLoadBalancingStrategy() {
        return connectionPoolLoadBalancingStrategy;
    }

    @Override
    public String getTransportStrategy() {
        return transportStrategy;
    }

    @Override
    public Boolean getAsync() {
        return async;
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
    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setSmtpServerHost(String smtpServerHost) {
        this.smtpServerHost = smtpServerHost;
    }

    public void setSmtpServerPort(Integer smtpServerPort) {
        this.smtpServerPort = smtpServerPort;
    }

    public void setSmtpUsername(Integer smtpUsername) {
        this.smtpUsername = smtpUsername;
    }

    public void setSmtpPassword(Integer smtpPassword) {
        this.smtpPassword = smtpPassword;
    }

    public void setClearEmailValidator(Boolean clearEmailValidator) {
        this.clearEmailValidator = clearEmailValidator;
    }

    public void setEmailValidator(EmailValidator emailValidator) {
        this.emailValidator = emailValidator;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void setConnectionPoolCoreSize(Integer connectionPoolCoreSize) {
        this.connectionPoolCoreSize = connectionPoolCoreSize;
    }

    public void setConnectionPoolMaxSize(Integer connectionPoolMaxSize) {
        this.connectionPoolMaxSize = connectionPoolMaxSize;
    }

    public void setConnectionPoolExpireAfterMillis(Integer connectionPoolExpireAfterMillis) {
        this.connectionPoolExpireAfterMillis = connectionPoolExpireAfterMillis;
    }

    public void setSessionTimeout(Integer sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public void setConnectionPoolLoadBalancingStrategy(String connectionPoolLoadBalancingStrategy) {
        this.connectionPoolLoadBalancingStrategy = connectionPoolLoadBalancingStrategy;
    }

    public void setTransportStrategy(String transportStrategy) {
        this.transportStrategy = transportStrategy;
    }

    public void setAsync(Boolean async) {
        this.async = async;
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

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "MailConfParams{" +
                "smtpServerHost='" + smtpServerHost + '\'' +
                ", smtpServerPort=" + smtpServerPort +
                ", smtpUsername=" + smtpUsername +
                ", smtpPassword=" + smtpPassword +
                ", clearEmailValidator=" + clearEmailValidator +
                ", emailValidator=" + emailValidator +
                ", executorService=" + executorService +
                ", connectionPoolCoreSize=" + connectionPoolCoreSize +
                ", connectionPoolMaxSize=" + connectionPoolMaxSize +
                ", connectionPoolExpireAfterMillis=" + connectionPoolExpireAfterMillis +
                ", sessionTimeout=" + sessionTimeout +
                ", connectionPoolLoadBalancingStrategy='" + connectionPoolLoadBalancingStrategy + '\'' +
                ", transportStrategy='" + transportStrategy + '\'' +
                ", async=" + async +
                ", domainKeyFile='" + domainKeyFile + '\'' +
                ", domain='" + domain + '\'' +
                ", selector='" + selector + '\'' +
                ", debugLogging=" + debugLogging +
                ", properties=" + properties +
                '}';
    }

}

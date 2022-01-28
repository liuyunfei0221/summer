package com.blue.mail.api.conf;

import java.util.List;

/**
 * @author liuyunfei
 * @date 2022/1/4
 * @apiNote
 */
@SuppressWarnings({"unused", "AlibabaLowerCamelCaseVariableNaming", "AlibabaAbstractClassShouldStartWithAbstractNaming"})
public abstract class MailSenderConfParams implements MailSenderConf {

    protected List<SmtpAttr> smtpAttrs;

    protected Integer corePoolSize;

    protected Integer maximumPoolSize;

    protected Long keepAliveSeconds;

    protected Integer blockingQueueCapacity;

    protected String threadNamePre;

    protected Integer bufferSize;

    protected List<String> throwableForRetry;

    protected Integer retryTimes;

    protected Boolean withDKIM;

    protected String domainKeyFile;

    protected String domain;

    protected String selector;

    protected Boolean debug;

    public MailSenderConfParams() {
    }

    @Override
    public List<SmtpAttr> getSmtpAttrs() {
        return smtpAttrs;
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
    public Integer getBufferSize() {
        return bufferSize;
    }

    @Override
    public List<String> getThrowableForRetry() {
        return throwableForRetry;
    }

    @Override
    public Integer getRetryTimes() {
        return retryTimes;
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
    public Boolean getDebug() {
        return debug;
    }

    public void setSmtpAttrs(List<SmtpAttr> smtpAttrs) {
        this.smtpAttrs = smtpAttrs;
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

    public void setBufferSize(Integer bufferSize) {
        this.bufferSize = bufferSize;
    }

    public void setThrowableForRetry(List<String> throwableForRetry) {
        this.throwableForRetry = throwableForRetry;
    }

    public void setRetryTimes(Integer retryTimes) {
        this.retryTimes = retryTimes;
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

    public void setDebug(Boolean debug) {
        this.debug = debug;
    }

    @Override
    public String toString() {
        return "MailSenderConfParams{" +
                "smtpAttrs=" + smtpAttrs +
                ", corePoolSize=" + corePoolSize +
                ", maximumPoolSize=" + maximumPoolSize +
                ", keepAliveSeconds=" + keepAliveSeconds +
                ", blockingQueueCapacity=" + blockingQueueCapacity +
                ", threadNamePre='" + threadNamePre + '\'' +
                ", bufferSize=" + bufferSize +
                ", throwableForRetry=" + throwableForRetry +
                ", retryTimes=" + retryTimes +
                ", withDKIM=" + withDKIM +
                ", domainKeyFile='" + domainKeyFile + '\'' +
                ", domain='" + domain + '\'' +
                ", selector='" + selector + '\'' +
                ", debug=" + debug +
                '}';
    }

}

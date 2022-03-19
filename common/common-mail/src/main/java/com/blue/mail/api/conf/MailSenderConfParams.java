package com.blue.mail.api.conf;

import java.util.List;

/**
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AlibabaLowerCamelCaseVariableNaming", "AlibabaAbstractClassShouldStartWithAbstractNaming"})
public abstract class MailSenderConfParams implements MailSenderConf {

    protected transient List<SenderAttr> senderAttrs;

    protected Integer corePoolSize;

    protected Integer maximumPoolSize;

    protected Long keepAliveSeconds;

    protected Integer blockingQueueCapacity;

    protected String threadNamePre;

    protected Integer bufferSize;

    protected List<String> throwableForRetry;

    protected Integer retryTimes;

    protected Boolean withDKIM;

    protected transient String domainKeyFile;

    protected transient String domain;

    protected transient String selector;

    public MailSenderConfParams() {
    }

    @Override
    public List<SenderAttr> getSmtpAttrs() {
        return senderAttrs;
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

    public void setSmtpAttrs(List<SenderAttr> senderAttrs) {
        this.senderAttrs = senderAttrs;
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

    @Override
    public String toString() {
        return "MailSenderConfParams{" +
                "smtpAttrs=" + senderAttrs +
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
                '}';
    }

}

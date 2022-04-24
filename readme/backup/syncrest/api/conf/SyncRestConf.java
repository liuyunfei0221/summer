package com.blue.base.component.syncrest.api.conf;

/**
 * rest conf
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public interface SyncRestConf {

    Integer getMaxPerRoute();

    Integer getMaxTotal();

    Integer getSoTimeout();

    Boolean getTcpNoDelay();

    Boolean getSoKeepAlive();

    Boolean getSoReuseAddress();

    Integer getRequestTimeout();

    Integer getConnectTimeout();

    Boolean getExpectContinueEnabled();

    Boolean getRedirectsEnabled();

    Boolean getCircularRedirectsAllowed();

    Integer getMaxRedirects();

    Integer getRetryTimes();

    String getUserAgent();

    Boolean getRadicalizationTry();

}

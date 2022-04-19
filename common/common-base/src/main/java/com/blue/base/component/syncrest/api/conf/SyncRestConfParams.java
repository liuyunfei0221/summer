package com.blue.base.component.syncrest.api.conf;

/**
 * rest conf param
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public class SyncRestConfParams implements SyncRestConf {

    protected Integer maxPerRoute;

    protected Integer maxTotal;

    protected Integer soTimeout;

    protected Boolean tcpNoDelay;

    protected Boolean soKeepAlive;

    protected Boolean soReuseAddress;

    protected Integer connectTimeout;

    protected Integer requestTimeout;

    protected Boolean expectContinueEnabled;

    protected Boolean redirectsEnabled;

    protected Boolean circularRedirectsAllowed;

    protected Integer maxRedirects;

    protected Integer retryTimes;

    protected String userAgent;

    protected Boolean radicalizationTry;

    public SyncRestConfParams() {
    }

    public SyncRestConfParams(Integer maxPerRoute, Integer maxTotal, Integer soTimeout, Boolean tcpNoDelay,
                              Boolean soKeepAlive, Boolean soReuseAddress, Integer connectTimeout, Integer requestTimeout,
                              Boolean expectContinueEnabled, Boolean redirectsEnabled, Boolean circularRedirectsAllowed,
                              Integer maxRedirects, Integer retryTimes, String userAgent, Boolean radicalizationTry) {
        this.maxPerRoute = maxPerRoute;
        this.maxTotal = maxTotal;
        this.soTimeout = soTimeout;
        this.tcpNoDelay = tcpNoDelay;
        this.soKeepAlive = soKeepAlive;
        this.soReuseAddress = soReuseAddress;
        this.connectTimeout = connectTimeout;
        this.requestTimeout = requestTimeout;
        this.expectContinueEnabled = expectContinueEnabled;
        this.redirectsEnabled = redirectsEnabled;
        this.circularRedirectsAllowed = circularRedirectsAllowed;
        this.maxRedirects = maxRedirects;
        this.retryTimes = retryTimes;
        this.userAgent = userAgent;
        this.radicalizationTry = radicalizationTry;
    }

    @Override
    public Integer getMaxPerRoute() {
        return maxPerRoute;
    }

    @Override
    public Integer getMaxTotal() {
        return maxTotal;
    }

    @Override
    public Integer getSoTimeout() {
        return soTimeout;
    }

    @Override
    public Boolean getTcpNoDelay() {
        return tcpNoDelay;
    }

    @Override
    public Boolean getSoKeepAlive() {
        return soKeepAlive;
    }

    @Override
    public Boolean getSoReuseAddress() {
        return soReuseAddress;
    }

    @Override
    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    @Override
    public Integer getRequestTimeout() {
        return requestTimeout;
    }

    @Override
    public Boolean getExpectContinueEnabled() {
        return expectContinueEnabled;
    }

    @Override
    public Boolean getRedirectsEnabled() {
        return redirectsEnabled;
    }

    @Override
    public Boolean getCircularRedirectsAllowed() {
        return circularRedirectsAllowed;
    }

    @Override
    public Integer getMaxRedirects() {
        return maxRedirects;
    }

    @Override
    public Integer getRetryTimes() {
        return retryTimes;
    }

    @Override
    public String getUserAgent() {
        return userAgent;
    }

    @Override
    public Boolean getRadicalizationTry() {
        return radicalizationTry;
    }

    public void setMaxPerRoute(Integer maxPerRoute) {
        this.maxPerRoute = maxPerRoute;
    }

    public void setMaxTotal(Integer maxTotal) {
        this.maxTotal = maxTotal;
    }

    public void setSoTimeout(Integer soTimeout) {
        this.soTimeout = soTimeout;
    }

    public void setTcpNoDelay(Boolean tcpNoDelay) {
        this.tcpNoDelay = tcpNoDelay;
    }

    public void setSoKeepAlive(Boolean soKeepAlive) {
        this.soKeepAlive = soKeepAlive;
    }

    public void setSoReuseAddress(Boolean soReuseAddress) {
        this.soReuseAddress = soReuseAddress;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public void setRequestTimeout(Integer requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public void setExpectContinueEnabled(Boolean expectContinueEnabled) {
        this.expectContinueEnabled = expectContinueEnabled;
    }

    public void setRedirectsEnabled(Boolean redirectsEnabled) {
        this.redirectsEnabled = redirectsEnabled;
    }

    public void setCircularRedirectsAllowed(Boolean circularRedirectsAllowed) {
        this.circularRedirectsAllowed = circularRedirectsAllowed;
    }

    public void setMaxRedirects(Integer maxRedirects) {
        this.maxRedirects = maxRedirects;
    }

    public void setRetryTimes(Integer retryTimes) {
        this.retryTimes = retryTimes;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setRadicalizationTry(Boolean radicalizationTry) {
        this.radicalizationTry = radicalizationTry;
    }

    @Override
    public String toString() {
        return "SyncRestConfParams{" +
                "maxPerRoute=" + maxPerRoute +
                ", maxTotal=" + maxTotal +
                ", soTimeout=" + soTimeout +
                ", tcpNoDelay=" + tcpNoDelay +
                ", soKeepAlive=" + soKeepAlive +
                ", soReuseAddress=" + soReuseAddress +
                ", connectTimeout=" + connectTimeout +
                ", requestTimeout=" + requestTimeout +
                ", expectContinueEnabled=" + expectContinueEnabled +
                ", redirectsEnabled=" + redirectsEnabled +
                ", circularRedirectsAllowed=" + circularRedirectsAllowed +
                ", maxRedirects=" + maxRedirects +
                ", retryTimes=" + retryTimes +
                ", userAgent='" + userAgent + '\'' +
                ", radicalizationTry=" + radicalizationTry +
                '}';
    }

}

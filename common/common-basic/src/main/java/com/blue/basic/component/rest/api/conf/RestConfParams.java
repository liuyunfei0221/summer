package com.blue.basic.component.rest.api.conf;

import reactor.netty.http.HttpProtocol;

import java.util.List;
import java.util.Map;

/**
 * reactive rest conf param
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused"})
public class RestConfParams implements RestConf {

    protected Integer maxConnections;

    protected Integer workerCount;

    protected Boolean useGlobalResources;

    protected Integer connectTimeoutMillis;

    protected Boolean useTcpNoDelay;

    protected List<HttpProtocol> protocols;

    protected Integer responseTimeoutMillis;

    protected Integer readTimeoutMillis;

    protected Integer writeTimeoutMillis;

    protected Integer maxByteInMemorySize;

    protected Map<String, String> defaultHeaders;

    public RestConfParams() {
    }

    public RestConfParams(Integer maxConnections, Integer workerCount, Boolean useGlobalResources,
                          Integer connectTimeoutMillis, Boolean useTcpNoDelay, List<HttpProtocol> protocols,
                          Integer responseTimeoutMillis, Integer readTimeoutMillis, Integer writeTimeoutMillis,
                          Integer maxByteInMemorySize, Map<String, String> defaultHeaders) {
        this.maxConnections = maxConnections;
        this.workerCount = workerCount;
        this.useGlobalResources = useGlobalResources;
        this.connectTimeoutMillis = connectTimeoutMillis;
        this.useTcpNoDelay = useTcpNoDelay;
        this.protocols = protocols;
        this.responseTimeoutMillis = responseTimeoutMillis;
        this.readTimeoutMillis = readTimeoutMillis;
        this.writeTimeoutMillis = writeTimeoutMillis;
        this.maxByteInMemorySize = maxByteInMemorySize;
        this.defaultHeaders = defaultHeaders;
    }

    @Override
    public Integer getMaxConnections() {
        return maxConnections;
    }

    @Override
    public Integer getWorkerCount() {
        return workerCount;
    }

    @Override
    public Boolean getUseGlobalResources() {
        return useGlobalResources;
    }

    @Override
    public Integer getConnectTimeoutMillis() {
        return connectTimeoutMillis;
    }

    @Override
    public Boolean getUseTcpNoDelay() {
        return useTcpNoDelay;
    }

    @Override
    public List<HttpProtocol> getProtocols() {
        return protocols;
    }

    @Override
    public Integer getResponseTimeoutMillis() {
        return responseTimeoutMillis;
    }

    @Override
    public Integer getReadTimeoutMillis() {
        return readTimeoutMillis;
    }

    @Override
    public Integer getWriteTimeoutMillis() {
        return writeTimeoutMillis;
    }

    @Override
    public Integer getMaxByteInMemorySize() {
        return maxByteInMemorySize;
    }

    @Override
    public Map<String, String> getDefaultHeaders() {
        return defaultHeaders;
    }

    public void setMaxConnections(Integer maxConnections) {
        this.maxConnections = maxConnections;
    }

    public void setWorkerCount(Integer workerCount) {
        this.workerCount = workerCount;
    }

    public void setUseGlobalResources(Boolean useGlobalResources) {
        this.useGlobalResources = useGlobalResources;
    }

    public void setConnectTimeoutMillis(Integer connectTimeoutMillis) {
        this.connectTimeoutMillis = connectTimeoutMillis;
    }

    public void setUseTcpNoDelay(Boolean useTcpNoDelay) {
        this.useTcpNoDelay = useTcpNoDelay;
    }

    public void setProtocols(List<HttpProtocol> protocols) {
        this.protocols = protocols;
    }

    public void setResponseTimeoutMillis(Integer responseTimeoutMillis) {
        this.responseTimeoutMillis = responseTimeoutMillis;
    }

    public void setReadTimeoutMillis(Integer readTimeoutMillis) {
        this.readTimeoutMillis = readTimeoutMillis;
    }

    public void setWriteTimeoutMillis(Integer writeTimeoutMillis) {
        this.writeTimeoutMillis = writeTimeoutMillis;
    }

    public void setMaxByteInMemorySize(Integer maxByteInMemorySize) {
        this.maxByteInMemorySize = maxByteInMemorySize;
    }

    public void setDefaultHeaders(Map<String, String> defaultHeaders) {
        this.defaultHeaders = defaultHeaders;
    }

    @Override
    public String toString() {
        return "RestConfParams{" +
                "maxConnections=" + maxConnections +
                ", workerCount=" + workerCount +
                ", useGlobalResources=" + useGlobalResources +
                ", connectTimeoutMillis=" + connectTimeoutMillis +
                ", useTcpNoDelay=" + useTcpNoDelay +
                ", protocols=" + protocols +
                ", responseTimeoutMillis=" + responseTimeoutMillis +
                ", readTimeoutMillis=" + readTimeoutMillis +
                ", writeTimeoutMillis=" + writeTimeoutMillis +
                ", maxByteInMemorySize=" + maxByteInMemorySize +
                ", defaultHeaders=" + defaultHeaders +
                '}';
    }

}
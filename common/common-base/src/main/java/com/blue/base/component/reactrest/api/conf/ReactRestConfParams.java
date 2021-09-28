package com.blue.base.component.reactrest.api.conf;

import reactor.netty.http.HttpProtocol;

import java.util.List;

/**
 * reactive rest conf param
 *
 * @author liuyunfei
 * @date 2021/9/9
 * @apiNote
 */
@SuppressWarnings({"SpellCheckingInspection", "unused"})
public class ReactRestConfParams implements ReactRestConf {

    protected Integer maxConnections;

    protected Integer workerCount;

    protected Boolean useGlobalResources;

    protected Integer connectTimeoutMillis;

    protected Boolean useTcpNodelay;

    protected List<HttpProtocol> protocols;

    protected Integer responseTimeoutMillis;

    protected Integer readTimeoutMillis;

    protected Integer writeTimeoutMillis;

    protected Integer maxByteInMemorySize;

    public ReactRestConfParams() {
    }

    public ReactRestConfParams(Integer maxConnections, Integer workerCount, Boolean useGlobalResources,
                               Integer connectTimeoutMillis, Boolean useTcpNodelay, List<HttpProtocol> protocols,
                               Integer responseTimeoutMillis, Integer readTimeoutMillis, Integer writeTimeoutMillis,
                               Integer maxByteInMemorySize) {
        this.maxConnections = maxConnections;
        this.workerCount = workerCount;
        this.useGlobalResources = useGlobalResources;
        this.connectTimeoutMillis = connectTimeoutMillis;
        this.useTcpNodelay = useTcpNodelay;
        this.protocols = protocols;
        this.responseTimeoutMillis = responseTimeoutMillis;
        this.readTimeoutMillis = readTimeoutMillis;
        this.writeTimeoutMillis = writeTimeoutMillis;
        this.maxByteInMemorySize = maxByteInMemorySize;
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
    public Boolean getUseTcpNodelay() {
        return useTcpNodelay;
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

    public void setUseTcpNodelay(Boolean useTcpNodelay) {
        this.useTcpNodelay = useTcpNodelay;
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

    @Override
    public String toString() {
        return "ReactRestConfParams{" +
                "maxConnections=" + maxConnections +
                ", workerCount=" + workerCount +
                ", useGlobalResources=" + useGlobalResources +
                ", connectTimeoutMillis=" + connectTimeoutMillis +
                ", useTcpNodelay=" + useTcpNodelay +
                ", protocols=" + protocols +
                ", responseTimeoutMillis=" + responseTimeoutMillis +
                ", readTimeoutMillis=" + readTimeoutMillis +
                ", writeTimeoutMillis=" + writeTimeoutMillis +
                ", maxByteInMemorySize=" + maxByteInMemorySize +
                '}';
    }

}

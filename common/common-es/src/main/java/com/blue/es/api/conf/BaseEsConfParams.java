package com.blue.es.api.conf;

import org.elasticsearch.client.NodeSelector;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import java.util.List;

/**
 * es params
 *
 * @author liuyunfei
 * @date 2021/9/17
 * @apiNote
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public abstract class BaseEsConfParams implements EsConf {

    transient List<EsNode> esNodes;

    transient List<DefaultHeader> defaultHeaders;

    transient String pathPrefix;

    Boolean strictDeprecationMode;

    Boolean metaHeaderEnabled;

    Boolean compressionEnabled;

    public BaseEsConfParams() {
    }

    @Override
    public List<EsNode> getEsNodes() {
        return esNodes;
    }

    @Override
    public List<DefaultHeader> getDefaultHeaders() {
        return defaultHeaders;
    }

    @Override
    public abstract RestClient.FailureListener getFailureListener();

    @Override
    public abstract RestClientBuilder.HttpClientConfigCallback getHttpClientConfigCallback();

    @Override
    public abstract RestClientBuilder.RequestConfigCallback getRequestConfigCallback();

    @Override
    public String getPathPrefix() {
        return pathPrefix;
    }

    @Override
    public Boolean getStrictDeprecationMode() {
        return strictDeprecationMode;
    }

    @Override
    public Boolean getMetaHeaderEnabled() {
        return metaHeaderEnabled;
    }

    @Override
    public Boolean getCompressionEnabled() {
        return compressionEnabled;
    }

    @Override
    public abstract NodeSelector getNodeSelector();

    public void setEsNodes(List<EsNode> esNodes) {
        this.esNodes = esNodes;
    }

    public void setDefaultHeaders(List<DefaultHeader> defaultHeaders) {
        this.defaultHeaders = defaultHeaders;
    }

    public void setPathPrefix(String pathPrefix) {
        this.pathPrefix = pathPrefix;
    }

    public void setStrictDeprecationMode(Boolean strictDeprecationMode) {
        this.strictDeprecationMode = strictDeprecationMode;
    }

    public void setMetaHeaderEnabled(Boolean metaHeaderEnabled) {
        this.metaHeaderEnabled = metaHeaderEnabled;
    }

    public void setCompressionEnabled(Boolean compressionEnabled) {
        this.compressionEnabled = compressionEnabled;
    }

    @Override
    public String toString() {
        return "BaseEsConfParams{" +
                "esNodes=" + esNodes +
                ", defaultHeaders=" + defaultHeaders +
                ", pathPrefix='" + pathPrefix + '\'' +
                ", strictDeprecationMode=" + strictDeprecationMode +
                ", metaHeaderEnabled=" + metaHeaderEnabled +
                ", compressionEnabled=" + compressionEnabled +
                '}';
    }

}

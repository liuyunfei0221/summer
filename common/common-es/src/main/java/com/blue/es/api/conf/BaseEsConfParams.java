package com.blue.es.api.conf;

import co.elastic.clients.transport.TransportOptions;
import org.elasticsearch.client.NodeSelector;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import java.util.List;

/**
 * es params
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public abstract class BaseEsConfParams implements EsConf {

    protected transient List<EsNode> esNodes;

    protected transient Boolean auth;

    protected transient String userName;

    protected transient String password;

    protected transient String certPath;

    protected transient List<DefaultHeader> defaultHeaders;

    protected transient String pathPrefix;

    protected Boolean strictDeprecationMode;

    protected Boolean metaHeaderEnabled;

    protected Boolean compressionEnabled;

    public BaseEsConfParams() {
    }

    @Override
    public List<EsNode> getEsNodes() {
        return esNodes;
    }

    @Override
    public Boolean getAuth() {
        return auth;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getCertPath() {
        return certPath;
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

    @Override
    public abstract TransportOptions getTransportOptions();

    public void setEsNodes(List<EsNode> esNodes) {
        this.esNodes = esNodes;
    }

    public void setAuth(Boolean auth) {
        this.auth = auth;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCertPath(String certPath) {
        this.certPath = certPath;
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
                ", auth=" + auth +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", certPath='" + certPath + '\'' +
                ", defaultHeaders=" + defaultHeaders +
                ", pathPrefix='" + pathPrefix + '\'' +
                ", strictDeprecationMode=" + strictDeprecationMode +
                ", metaHeaderEnabled=" + metaHeaderEnabled +
                ", compressionEnabled=" + compressionEnabled +
                '}';
    }

}

package com.blue.es.api.conf;

import co.elastic.clients.transport.TransportOptions;
import org.elasticsearch.client.NodeSelector;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import java.util.List;

/**
 * es conf
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public interface EsConf {

    List<EsNode> getEsNodes();

    List<DefaultHeader> getDefaultHeaders();

    RestClient.FailureListener getFailureListener();

    RestClientBuilder.HttpClientConfigCallback getHttpClientConfigCallback();

    RestClientBuilder.RequestConfigCallback getRequestConfigCallback();

    String getPathPrefix();

    Boolean getStrictDeprecationMode();

    Boolean getMetaHeaderEnabled();

    Boolean getCompressionEnabled();

    NodeSelector getNodeSelector();

    TransportOptions getTransportOptions();
}

package com.blue.es.api.conf;

import org.elasticsearch.client.NodeSelector;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import java.util.List;

/**
 * es配置
 *
 * @author liuyunfei
 * @date 2021/9/17
 * @apiNote
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
}

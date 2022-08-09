package com.blue.finance.config.blue;

import co.elastic.clients.transport.TransportOptions;
import com.blue.es.api.conf.BaseEsConfParams;
import org.elasticsearch.client.NodeSelector;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * es config
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "es")
public class BlueEsConfig extends BaseEsConfParams {

    @Override
    public RestClient.FailureListener getFailureListener() {
        return null;
    }

    @Override
    public RestClientBuilder.HttpClientConfigCallback getHttpClientConfigCallback() {
        return null;
    }

    @Override
    public RestClientBuilder.RequestConfigCallback getRequestConfigCallback() {
        return null;
    }

    @Override
    public NodeSelector getNodeSelector() {
        return null;
    }

    @Override
    public TransportOptions getTransportOptions() {
        return null;
    }
}

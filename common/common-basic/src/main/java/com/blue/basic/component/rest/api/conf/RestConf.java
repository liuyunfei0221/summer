package com.blue.basic.component.rest.api.conf;

import reactor.netty.http.HttpProtocol;

import java.util.List;
import java.util.Map;

/**
 * reactive rest conf
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public interface RestConf {

    Integer getMaxConnections();

    Integer getWorkerCount();

    Boolean getUseGlobalResources();

    Integer getConnectTimeoutMillis();

    Boolean getUseTcpNoDelay();

    List<HttpProtocol> getProtocols();

    Integer getResponseTimeoutMillis();

    Integer getReadTimeoutMillis();

    Integer getWriteTimeoutMillis();

    Integer getMaxByteInMemorySize();

    Map<String, String> getDefaultHeaders();

}
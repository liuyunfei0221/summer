package com.blue.base.component.reactrest.api.conf;

import reactor.netty.http.HttpProtocol;

import java.util.List;

/**
 * rest参数配置
 *
 * @author liuyunfei
 * @date 2021/9/9
 * @apiNote
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
public interface ReactRestConf {

    Integer getMaxConnections();

    Integer getWorkerCount();

    Boolean getUseGlobalResources();

    Integer getConnectTimeoutMillis();

    Boolean getUseTcpNodelay();

    List<HttpProtocol> getProtocols();

    Integer getResponseTimeoutMillis();

    Integer getReadTimeoutMillis();

    Integer getWriteTimeoutMillis();

    Integer getMaxByteInMemorySize();

}

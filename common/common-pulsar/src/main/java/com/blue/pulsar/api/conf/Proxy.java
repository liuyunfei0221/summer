package com.blue.pulsar.api.conf;

import org.apache.pulsar.client.api.ProxyProtocol;

/**
 * proxy attrs
 *
 * @author liuyunfei
 */
public final class Proxy {

    private String proxyServiceUrl;

    private ProxyProtocol proxyProtocol;

    public Proxy() {
    }

    public String getProxyServiceUrl() {
        return proxyServiceUrl;
    }

    public void setProxyServiceUrl(String proxyServiceUrl) {
        this.proxyServiceUrl = proxyServiceUrl;
    }

    public ProxyProtocol getProxyProtocol() {
        return proxyProtocol;
    }

    public void setProxyProtocol(ProxyProtocol proxyProtocol) {
        this.proxyProtocol = proxyProtocol;
    }

    @Override
    public String toString() {
        return "Proxy{" +
                "proxyServiceUrl='" + proxyServiceUrl + '\'' +
                ", proxyProtocol=" + proxyProtocol +
                '}';
    }

}

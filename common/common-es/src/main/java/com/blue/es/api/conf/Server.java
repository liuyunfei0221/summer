package com.blue.es.api.conf;

import java.util.Objects;

import static com.blue.basic.common.base.BlueChecker.isNull;

/**
 * es server
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "unused"})
public final class Server {

    private transient String host;

    private transient Integer port;

    private transient String schema;

    public Server() {
    }

    public Server(String host, Integer port, String schema) {
        this.host = host;
        this.port = port;
        this.schema = schema;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (isNull(o) || getClass() != o.getClass()) return false;
        Server server = (Server) o;
        return Objects.equals(host, server.host) && Objects.equals(port, server.port) && Objects.equals(schema, server.schema);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port, schema);
    }
}

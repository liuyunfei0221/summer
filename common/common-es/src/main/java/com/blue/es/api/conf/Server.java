package com.blue.es.api.conf;

import java.util.Objects;

/**
 * es server
 *
 * @author liuyunfei
 * @date 2021/9/19
 * @apiNote
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "unused"})
public final class Server {

    private String host;

    private Integer port;

    private String schema;

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
        if (o == null || getClass() != o.getClass()) return false;
        Server server = (Server) o;
        return Objects.equals(host, server.host) && Objects.equals(port, server.port) && Objects.equals(schema, server.schema);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port, schema);
    }
}

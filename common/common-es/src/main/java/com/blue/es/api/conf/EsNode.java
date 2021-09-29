package com.blue.es.api.conf;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * es node
 *
 * @author liuyunfei
 * @date 2021/9/20
 * @apiNote
 */
@SuppressWarnings("unused")
public final class EsNode {

    private Server server;

    private Set<Server> boundServers;

    private String name;

    private String version;

    private List<String> roles;

    private Map<String, List<String>> attributes;

    public EsNode() {
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Set<Server> getBoundServers() {
        return boundServers;
    }

    public void setBoundServers(Set<Server> boundServers) {
        this.boundServers = boundServers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public Map<String, List<String>> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, List<String>> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return "EsNode{" +
                "server=" + server +
                ", boundServers=" + boundServers +
                ", name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", roles=" + roles +
                ", attributes=" + attributes +
                '}';
    }

}

package com.blue.base.model.base;

import java.io.Serializable;

/**
 * illegal mark event
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class IllegalMarkEvent implements Serializable {

    private static final long serialVersionUID = -1983751954933572385L;

    /**
     * jwt str
     */
    private String jwt;

    /**
     * target ip
     */
    private String ip;

    /**
     * resource for intercept
     */
    private String resource;

    /**
     * actions for intercept
     */
    private Boolean mark;

    public IllegalMarkEvent() {
    }

    public IllegalMarkEvent(String jwt, String ip, String resource, Boolean mark) {
        this.jwt = jwt;
        this.ip = ip;
        this.resource = resource;
        this.mark = mark;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public Boolean getMark() {
        return mark;
    }

    public void setMark(Boolean mark) {
        this.mark = mark;
    }

    @Override
    public String toString() {
        return "IllegalMarkEvent{" +
                "jwt='" + jwt + '\'' +
                ", ip='" + ip + '\'' +
                ", resource='" + resource + '\'' +
                ", mark=" + mark +
                '}';
    }

}

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
     * actions for intercept
     */
    private Boolean mark;

    public IllegalMarkEvent() {
    }

    public IllegalMarkEvent(String jwt, String ip, Boolean mark) {
        this.jwt = jwt;
        this.ip = ip;
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
                ", mark=" + mark +
                '}';
    }
}

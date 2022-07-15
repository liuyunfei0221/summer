package com.blue.basic.model.common;

import java.io.Serializable;

/**
 * illegal mark event
 *
 * @author liuyunfei
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
    private String resourceKey;

    /**
     * actions for intercept
     */
    private Boolean mark;

    /**
     * expire seconds
     */
    private Long illegalExpiresSecond;

    public IllegalMarkEvent() {
    }

    public IllegalMarkEvent(String jwt, String ip, String resourceKey, Boolean mark, Long illegalExpiresSecond) {
        this.jwt = jwt;
        this.ip = ip;
        this.resourceKey = resourceKey;
        this.mark = mark;
        this.illegalExpiresSecond = illegalExpiresSecond;
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

    public String getResourceKey() {
        return resourceKey;
    }

    public void setResourceKey(String resourceKey) {
        this.resourceKey = resourceKey;
    }

    public Boolean getMark() {
        return mark;
    }

    public void setMark(Boolean mark) {
        this.mark = mark;
    }

    public Long getIllegalExpiresSecond() {
        return illegalExpiresSecond;
    }

    public void setIllegalExpiresSecond(Long illegalExpiresSecond) {
        this.illegalExpiresSecond = illegalExpiresSecond;
    }

    @Override
    public String toString() {
        return "IllegalMarkEvent{" +
                "jwt='" + jwt + '\'' +
                ", ip='" + ip + '\'' +
                ", resourceKey='" + resourceKey + '\'' +
                ", mark=" + mark +
                ", illegalExpiresSecond=" + illegalExpiresSecond +
                '}';
    }

}

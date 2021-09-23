package com.blue.base.model.event.mark;

import com.blue.base.constant.base.IllegalReason;

import java.io.Serializable;

/**
 * 针对jwt串或ip的拦截标记事件
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class IllegalMarkEvent implements Serializable {

    private static final long serialVersionUID = -1983751954933572385L;

    /**
     * jwt串
     */
    private String jwt;

    /**
     * 目标ip
     */
    private String ip;

    /**
     * 拦截或取消拦截
     */
    private Boolean active;

    /**
     * 非法原因/如果active为true
     */
    private IllegalReason illegalReason;

    public IllegalMarkEvent() {
    }

    public IllegalMarkEvent(String jwt, String ip, Boolean active, IllegalReason illegalReason) {
        this.jwt = jwt;
        this.ip = ip;
        this.active = active;
        this.illegalReason = illegalReason;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public IllegalReason getIllegalReason() {
        return illegalReason;
    }

    public void setIllegalReason(IllegalReason illegalReason) {
        this.illegalReason = illegalReason;
    }

    @Override
    public String toString() {
        return "IllegalMarkEvent{" +
                "jwt='" + jwt + '\'' +
                ", ip='" + ip + '\'' +
                ", active=" + active +
                ", illegalReason=" + illegalReason +
                '}';
    }

}

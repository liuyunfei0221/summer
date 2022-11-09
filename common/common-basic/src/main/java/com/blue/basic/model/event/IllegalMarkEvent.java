package com.blue.basic.model.event;

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
     * member id str
     */
    private String memberId;

    /**
     * target ip
     */
    private String ip;

    /**
     * request method
     */
    private String method;

    /**
     * request uri
     */
    private String uri;

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

    public IllegalMarkEvent(String memberId, String ip, String method, String uri, Boolean mark, Long illegalExpiresSecond) {
        this.memberId = memberId;
        this.ip = ip;
        this.method = method;
        this.uri = uri;
        this.mark = mark;
        this.illegalExpiresSecond = illegalExpiresSecond;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
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
                "memberId='" + memberId + '\'' +
                ", ip='" + ip + '\'' +
                ", method='" + method + '\'' +
                ", uri='" + uri + '\'' +
                ", mark=" + mark +
                ", illegalExpiresSecond=" + illegalExpiresSecond +
                '}';
    }
    
}

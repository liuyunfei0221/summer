package com.blue.marketing.api.model;

import com.blue.base.constant.marketing.EventType;

import java.io.Serializable;

/**
 * 营销事件
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class MarketingEvent implements Serializable {

    private static final long serialVersionUID = -3232798896272586812L;

    /**
     * 事件类型
     */
    private EventType eventType;

    /**
     * 成员id
     */
    private Long memberId;

    /**
     * 事件的json
     */
    private String event;

    /**
     * 事件时间
     */
    private Long eventTime;

    public MarketingEvent() {
    }

    public MarketingEvent(EventType eventType, Long memberId, String event, Long eventTime) {
        this.eventType = eventType;
        this.memberId = memberId;
        this.event = event;
        this.eventTime = eventTime;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Long getEventTime() {
        return eventTime;
    }

    public void setEventTime(Long eventTime) {
        this.eventTime = eventTime;
    }

    @Override
    public String toString() {
        return "MarketingEvent{" +
                "eventType=" + eventType +
                ", memberId=" + memberId +
                ", event='" + event + '\'' +
                ", eventTime=" + eventTime +
                '}';
    }

}

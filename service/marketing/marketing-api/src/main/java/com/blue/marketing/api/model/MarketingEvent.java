package com.blue.marketing.api.model;

import java.io.Serializable;

/**
 * marketing event
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class MarketingEvent implements Serializable {

    private static final long serialVersionUID = -3232798896272586812L;

    /**
     * @see com.blue.basic.constant.marketing.MarketingEventType
     */
    private Integer marketingEventType;

    private Long memberId;

    /**
     * event json
     */
    private String event;

    private Long eventTime;

    public MarketingEvent() {
    }

    public MarketingEvent(Integer marketingEventType, Long memberId, String event, Long eventTime) {
        this.marketingEventType = marketingEventType;
        this.memberId = memberId;
        this.event = event;
        this.eventTime = eventTime;
    }

    public Integer getEventType() {
        return marketingEventType;
    }

    public void setEventType(Integer marketingEventType) {
        this.marketingEventType = marketingEventType;
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
                "eventType=" + marketingEventType +
                ", memberId=" + memberId +
                ", event='" + event + '\'' +
                ", eventTime=" + eventTime +
                '}';
    }

}

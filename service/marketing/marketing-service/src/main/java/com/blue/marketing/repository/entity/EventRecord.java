package com.blue.marketing.repository.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * marketing event entity
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
@Document(collection = "eventRecord")
public final class EventRecord implements Serializable {

    private static final long serialVersionUID = 4304981563007771288L;

    @Id
    private Long id;

    /**
     * member id
     */
    private Long memberId;

    /**
     * event type
     *
     * @see com.blue.basic.constant.marketing.MarketingEventType
     */
    private Integer type;

    /**
     * event json
     */
    private String data;

    /**
     * handling status
     *
     * @see com.blue.basic.constant.marketing.HandleStatus
     */
    private Integer status;

    /**
     * event time
     */
    private Long createTime;

    public EventRecord() {
    }

    public EventRecord(Long id, Long memberId, Integer type, String data, Integer status, Long createTime) {
        this.id = id;
        this.memberId = memberId;
        this.type = type;
        this.data = data;
        this.status = status;
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "EventRecord{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", type=" + type +
                ", data='" + data + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                '}';
    }

}
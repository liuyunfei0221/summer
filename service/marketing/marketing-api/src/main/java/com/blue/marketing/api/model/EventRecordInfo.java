package com.blue.marketing.api.model;


import com.blue.basic.serializer.Long2StringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * event info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class EventRecordInfo implements Serializable {

    private static final long serialVersionUID = 6569268133204313196L;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long id;

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
    @JsonSerialize(using = Long2StringSerializer.class)
    private Long createTime;

    /**
     * creator
     */
    @JsonSerialize(using = Long2StringSerializer.class)
    private Long creator;

    private String creatorName;

    public EventRecordInfo(Long id, Integer type, String data, Integer status, Long createTime, Long creator, String creatorName) {
        this.id = id;
        this.type = type;
        this.data = data;
        this.status = status;
        this.createTime = createTime;
        this.creator = creator;
        this.creatorName = creatorName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    @Override
    public String toString() {
        return "EventRecordInfo{" +
                "id=" + id +
                ", type=" + type +
                ", data='" + data + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", creator=" + creator +
                ", creatorName='" + creatorName + '\'' +
                '}';
    }

}

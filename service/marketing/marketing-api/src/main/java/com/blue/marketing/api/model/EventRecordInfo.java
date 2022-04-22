package com.blue.marketing.api.model;


import java.io.Serializable;

/**
 * event info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class EventRecordInfo implements Serializable {

    private static final long serialVersionUID = 6569268133204313196L;

    private final Long id;

    /**
     * event type
     *
     * @see com.blue.base.constant.marketing.MarketingEventType
     */
    private final Integer type;

    /**
     * event json
     */
    private final String data;

    /**
     * handling status
     *
     * @see com.blue.base.constant.marketing.HandleStatus
     */
    private final Integer status;

    /**
     * event time
     */
    private final Long createTime;

    /**
     * creator
     */
    private final Long creator;

    private final String creatorName;

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

    public Integer getType() {
        return type;
    }

    public String getData() {
        return data;
    }

    public Integer getStatus() {
        return status;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public Long getCreator() {
        return creator;
    }

    public String getCreatorName() {
        return creatorName;
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

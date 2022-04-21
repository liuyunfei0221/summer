package com.blue.marketing.repository.entity;

import java.io.Serializable;

import static com.blue.base.common.base.BlueChecker.isNull;

/**
 * marketing event entity
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class EventRecord implements Serializable {

    private static final long serialVersionUID = 4304981563007771288L;

    private Long id;

    /**
     * event type
     *
     * @see com.blue.base.constant.marketing.MarketingEventType
     */
    private Integer type;

    /**
     * event json
     */
    private String data;

    /**
     * handling status
     *
     * @see com.blue.base.constant.marketing.HandleStatus
     */
    private Integer status;

    /**
     * event time
     */
    private Long createTime;

    /**
     * creator
     */
    private Long creator;

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
        this.data = isNull(data) ? null : data.trim();
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

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", type=" + type +
                ", data='" + data + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", creator=" + creator +
                '}';
    }

}
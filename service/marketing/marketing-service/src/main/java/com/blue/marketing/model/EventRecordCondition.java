package com.blue.marketing.model;

import java.io.Serializable;

/**
 * event record condition for select
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class EventRecordCondition implements Serializable {

    private static final long serialVersionUID = -8708899408485179098L;

    private Long id;

    /**
     * event type
     *
     * @see com.blue.base.constant.marketing.MarketingEventType
     */
    private Integer type;

    /**
     * handling status
     *
     * @see com.blue.base.constant.marketing.HandleStatus
     */
    private Integer status;

    /**
     * creator
     */
    private Long creator;

    private Long createTimeBegin;

    private Long createTimeEnd;

    private String sortAttribute;

    private String sortType;

    public EventRecordCondition() {
    }

    public EventRecordCondition(Long id, Integer type, Integer status, Long creator, Long createTimeBegin, Long createTimeEnd, String sortAttribute, String sortType) {
        this.id = id;
        this.type = type;
        this.status = status;
        this.creator = creator;
        this.createTimeBegin = createTimeBegin;
        this.createTimeEnd = createTimeEnd;
        this.sortAttribute = sortAttribute;
        this.sortType = sortType;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public Long getCreateTimeBegin() {
        return createTimeBegin;
    }

    public void setCreateTimeBegin(Long createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }

    public Long getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(Long createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public String getSortAttribute() {
        return sortAttribute;
    }

    public void setSortAttribute(String sortAttribute) {
        this.sortAttribute = sortAttribute;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    @Override
    public String toString() {
        return "EventRecordCondition{" +
                "id=" + id +
                ", type=" + type +
                ", status=" + status +
                ", creator=" + creator +
                ", createTimeBegin=" + createTimeBegin +
                ", createTimeEnd=" + createTimeEnd +
                ", sortAttribute='" + sortAttribute + '\'' +
                ", sortType='" + sortType + '\'' +
                '}';
    }

}

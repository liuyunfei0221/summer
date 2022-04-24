package com.blue.marketing.model;

import com.blue.base.constant.base.SortType;
import com.blue.base.model.base.SortCondition;
import com.blue.marketing.constant.EventRecordSortAttribute;

import java.io.Serializable;

/**
 * event record condition for select
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class EventRecordCondition extends SortCondition implements Serializable {

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

    public EventRecordCondition() {
        super(EventRecordSortAttribute.ID.attribute, SortType.DESC.identity);
    }

    public EventRecordCondition(String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
    }

    public EventRecordCondition(String sortAttribute, String sortType, Long id, Integer type, Integer status, Long creator, Long createTimeBegin, Long createTimeEnd) {
        super(sortAttribute, sortType);
        this.id = id;
        this.type = type;
        this.status = status;
        this.creator = creator;
        this.createTimeBegin = createTimeBegin;
        this.createTimeEnd = createTimeEnd;
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

    @Override
    public String toString() {
        return "EventRecordCondition{" +
                "id=" + id +
                ", type=" + type +
                ", status=" + status +
                ", creator=" + creator +
                ", createTimeBegin=" + createTimeBegin +
                ", createTimeEnd=" + createTimeEnd +
                '}';
    }

}

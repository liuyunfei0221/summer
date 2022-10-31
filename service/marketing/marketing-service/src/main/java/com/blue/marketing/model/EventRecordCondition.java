package com.blue.marketing.model;

import com.blue.basic.model.common.SortCondition;
import com.blue.marketing.constant.EventRecordSortAttribute;

import java.io.Serializable;

import static com.blue.basic.constant.common.SortType.DESC;

/**
 * event record manager condition for select
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class EventRecordCondition extends SortCondition implements Serializable {

    private static final long serialVersionUID = -8708899408485179098L;

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

    private Long createTimeBegin;

    private Long createTimeEnd;

    public EventRecordCondition() {
        super(EventRecordSortAttribute.ID.attribute, DESC.identity);
    }

    public EventRecordCondition(String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
    }

    public EventRecordCondition(Integer type, String data, Integer status, Long createTimeBegin, Long createTimeEnd, String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
        this.type = type;
        this.data = data;
        this.status = status;
        this.createTimeBegin = createTimeBegin;
        this.createTimeEnd = createTimeEnd;
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
                "type=" + type +
                ", data='" + data + '\'' +
                ", status=" + status +
                ", createTimeBegin=" + createTimeBegin +
                ", createTimeEnd=" + createTimeEnd +
                ", sortAttribute='" + sortAttribute + '\'' +
                ", sortType='" + sortType + '\'' +
                '}';
    }

}

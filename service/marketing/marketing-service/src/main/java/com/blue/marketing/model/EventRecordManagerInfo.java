package com.blue.marketing.model;


import com.blue.basic.serializer.Long2StringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * event info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class EventRecordManagerInfo implements Serializable {

    private static final long serialVersionUID = 6569268133204313196L;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long id;

    /**
     * member id
     */
    @JsonSerialize(using = Long2StringSerializer.class)
    private Long memberId;

    private String memberName;

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

    public EventRecordManagerInfo() {
    }

    public EventRecordManagerInfo(Long id, Long memberId, String memberName, Integer type, String data, Integer status, Long createTime) {
        this.id = id;
        this.memberId = memberId;
        this.memberName = memberName;
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

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
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
        return "EventRecordInfo{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", type=" + type +
                ", data='" + data + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                '}';
    }

}

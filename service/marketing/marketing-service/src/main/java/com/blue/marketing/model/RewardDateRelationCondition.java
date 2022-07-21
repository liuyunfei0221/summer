package com.blue.marketing.model;

import com.blue.basic.model.common.SortCondition;
import com.blue.marketing.constant.EventRecordSortAttribute;

import java.io.Serializable;

import static com.blue.basic.constant.common.SortType.DESC;

/**
 * reward date relation condition for select
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class RewardDateRelationCondition extends SortCondition implements Serializable {

    private static final long serialVersionUID = -2895218233083070356L;

    private Long id;

    private Long rewardId;

    private Integer year;

    private Integer month;

    private Integer day;

    private Long createTimeBegin;

    private Long createTimeEnd;

    private Long updateTimeBegin;

    private Long updateTimeEnd;

    public RewardDateRelationCondition() {
        super(EventRecordSortAttribute.ID.attribute, DESC.identity);
    }

    public RewardDateRelationCondition(String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
    }

    public RewardDateRelationCondition(Long id, Long rewardId, Integer year, Integer month, Integer day,
                                       Long createTimeBegin, Long createTimeEnd, Long updateTimeBegin, Long updateTimeEnd,
                                       String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
        this.id = id;
        this.rewardId = rewardId;
        this.year = year;
        this.month = month;
        this.day = day;
        this.createTimeBegin = createTimeBegin;
        this.createTimeEnd = createTimeEnd;
        this.updateTimeBegin = updateTimeBegin;
        this.updateTimeEnd = updateTimeEnd;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRewardId() {
        return rewardId;
    }

    public void setRewardId(Long rewardId) {
        this.rewardId = rewardId;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
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

    public Long getUpdateTimeBegin() {
        return updateTimeBegin;
    }

    public void setUpdateTimeBegin(Long updateTimeBegin) {
        this.updateTimeBegin = updateTimeBegin;
    }

    public Long getUpdateTimeEnd() {
        return updateTimeEnd;
    }

    public void setUpdateTimeEnd(Long updateTimeEnd) {
        this.updateTimeEnd = updateTimeEnd;
    }

    @Override
    public String toString() {
        return "RewardDateRelationCondition{" +
                "id=" + id +
                ", rewardId=" + rewardId +
                ", year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", createTimeBegin=" + createTimeBegin +
                ", createTimeEnd=" + createTimeEnd +
                ", updateTimeBegin=" + updateTimeBegin +
                ", updateTimeEnd=" + updateTimeEnd +
                ", sortAttribute='" + sortAttribute + '\'' +
                ", sortType='" + sortType + '\'' +
                '}';
    }

}

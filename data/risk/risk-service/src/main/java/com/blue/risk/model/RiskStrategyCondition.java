package com.blue.risk.model;

import com.blue.basic.model.common.SortCondition;
import com.blue.risk.constant.RiskStrategySortAttribute;

import java.io.Serializable;

import static com.blue.basic.constant.common.SortType.DESC;

/**
 * risk strategy condition for select
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class RiskStrategyCondition extends SortCondition implements Serializable {

    private static final long serialVersionUID = -3160715509814789471L;

    private Long id;

    private String nameLike;

    /**
     * hit type
     *
     * @see com.blue.basic.constant.risk.RiskType
     */
    private Integer type;

    private Boolean enable;

    private Long createTimeBegin;

    private Long createTimeEnd;

    private Long updateTimeBegin;

    private Long updateTimeEnd;

    private Long creator;

    private Long updater;

    public RiskStrategyCondition() {
        super(RiskStrategySortAttribute.CREATE_TIME.attribute, DESC.identity);
    }

    public RiskStrategyCondition(String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
    }

    public RiskStrategyCondition(Long id, String nameLike, Integer type, Boolean enable, Long createTimeBegin, Long createTimeEnd, Long updateTimeBegin, Long updateTimeEnd, Long creator, Long updater,
                                 String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
        this.id = id;
        this.nameLike = nameLike;
        this.type = type;
        this.enable = enable;
        this.createTimeBegin = createTimeBegin;
        this.createTimeEnd = createTimeEnd;
        this.updateTimeBegin = updateTimeBegin;
        this.updateTimeEnd = updateTimeEnd;
        this.creator = creator;
        this.updater = updater;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameLike() {
        return nameLike;
    }

    public void setNameLike(String nameLike) {
        this.nameLike = nameLike;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
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

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public Long getUpdater() {
        return updater;
    }

    public void setUpdater(Long updater) {
        this.updater = updater;
    }

    @Override
    public String toString() {
        return "RiskStrategyCondition{" +
                "id=" + id +
                ", nameLike='" + nameLike + '\'' +
                ", type=" + type +
                ", enable=" + enable +
                ", createTimeBegin=" + createTimeBegin +
                ", createTimeEnd=" + createTimeEnd +
                ", updateTimeBegin=" + updateTimeBegin +
                ", updateTimeEnd=" + updateTimeEnd +
                ", creator=" + creator +
                ", updater=" + updater +
                ", sortAttribute='" + sortAttribute + '\'' +
                ", sortType='" + sortType + '\'' +
                '}';
    }
    
}

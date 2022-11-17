package com.blue.marketing.model;

import com.blue.basic.model.common.SortCondition;
import com.blue.marketing.constant.RewardSortAttribute;

import java.io.Serializable;

import static com.blue.basic.constant.common.SortType.DESC;

/**
 * reward condition for select
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class RewardCondition extends SortCondition implements Serializable {

    private static final long serialVersionUID = -8708899408485179098L;

    private Long id;

    private String nameLike;

    private String link;

    /**
     * reward type
     *
     * @see com.blue.basic.constant.marketing.RewardType
     */
    private Integer type;

    /**
     * reward status
     *
     * @see com.blue.basic.constant.common.Status
     */
    private Integer status;

    private Long createTimeBegin;

    private Long createTimeEnd;

    private Long updateTimeBegin;

    private Long updateTimeEnd;

    public RewardCondition() {
        super(RewardSortAttribute.CREATE_TIME.attribute, DESC.identity);
    }

    public RewardCondition(String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
    }

    public RewardCondition(Long id, String nameLike, String link, Integer type, Integer status, Long createTimeBegin, Long createTimeEnd,
                           Long updateTimeBegin, Long updateTimeEnd, String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
        this.id = id;
        this.nameLike = nameLike;
        this.link = link;
        this.type = type;
        this.status = status;
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

    public String getNameLike() {
        return nameLike;
    }

    public void setNameLike(String nameLike) {
        this.nameLike = nameLike;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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
        return "RewardCondition{" +
                "id=" + id +
                ", nameLike='" + nameLike + '\'' +
                ", link='" + link + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", createTimeBegin=" + createTimeBegin +
                ", createTimeEnd=" + createTimeEnd +
                ", updateTimeBegin=" + updateTimeBegin +
                ", updateTimeEnd=" + updateTimeEnd +
                ", sortAttribute='" + sortAttribute + '\'' +
                ", sortType='" + sortType + '\'' +
                '}';
    }

}

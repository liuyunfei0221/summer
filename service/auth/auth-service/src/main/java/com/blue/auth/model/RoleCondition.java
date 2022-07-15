package com.blue.auth.model;

import com.blue.auth.constant.ResourceSortAttribute;
import com.blue.basic.model.common.SortCondition;

import java.io.Serializable;

import static com.blue.basic.constant.common.SortType.DESC;

/**
 * role condition for select
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class RoleCondition extends SortCondition implements Serializable {

    private static final long serialVersionUID = -2623160339413516868L;

    private Long id;

    private Integer type;

    private String nameLike;

    private Long createTimeBegin;

    private Long createTimeEnd;

    private Long updateTimeBegin;

    private Long updateTimeEnd;

    public RoleCondition() {
        super(ResourceSortAttribute.ID.attribute, DESC.identity);
    }

    public RoleCondition(String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
    }

    public RoleCondition(Long id, Integer type, String nameLike, Long createTimeBegin, Long createTimeEnd, Long updateTimeBegin, Long updateTimeEnd, String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
        this.id = id;
        this.type = type;
        this.nameLike = nameLike;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getNameLike() {
        return nameLike;
    }

    public void setNameLike(String nameLike) {
        this.nameLike = nameLike;
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
        return "RoleCondition{" +
                "id=" + id +
                ", type=" + type +
                ", nameLike='" + nameLike + '\'' +
                ", createTimeBegin=" + createTimeBegin +
                ", createTimeEnd=" + createTimeEnd +
                ", updateTimeBegin=" + updateTimeBegin +
                ", updateTimeEnd=" + updateTimeEnd +
                ", sortAttribute='" + sortAttribute + '\'' +
                ", sortType='" + sortType + '\'' +
                '}';
    }

}

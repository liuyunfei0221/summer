package com.blue.auth.model;

import com.blue.auth.constant.ResourceSortAttribute;
import com.blue.base.constant.base.SortType;
import com.blue.base.model.base.SortCondition;

import java.io.Serializable;

/**
 * role condition for select
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class RoleCondition extends SortCondition implements Serializable {

    private static final long serialVersionUID = -2623160339413516868L;

    private Long id;

    private String name;

    private Long createTimeBegin;

    private Long createTimeEnd;

    private Long updateTimeBegin;

    private Long updateTimeEnd;

    public RoleCondition() {
        super(ResourceSortAttribute.ID.attribute, SortType.DESC.identity);
    }

    public RoleCondition(String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
    }

    public RoleCondition(String sortAttribute, String sortType, Long id, String name, Long createTimeBegin, Long createTimeEnd, Long updateTimeBegin, Long updateTimeEnd) {
        super(sortAttribute, sortType);
        this.id = id;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
                ", name='" + name + '\'' +
                ", createTimeBegin=" + createTimeBegin +
                ", createTimeEnd=" + createTimeEnd +
                ", updateTimeBegin=" + updateTimeBegin +
                ", updateTimeEnd=" + updateTimeEnd +
                '}';
    }

}

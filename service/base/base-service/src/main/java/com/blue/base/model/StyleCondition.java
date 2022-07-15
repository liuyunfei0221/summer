package com.blue.base.model;

import com.blue.base.constant.StyleSortAttribute;
import com.blue.basic.model.common.SortCondition;

import java.io.Serializable;

import static com.blue.basic.constant.common.SortType.DESC;

/**
 * style condition for select
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class StyleCondition extends SortCondition implements Serializable {

    private static final long serialVersionUID = 4088331103184874296L;

    private Long id;

    private String nameLike;

    /**
     * @see com.blue.basic.constant.portal.StyleType
     */
    private Integer type;

    private Boolean isActive;

    /**
     * @see com.blue.basic.constant.common.Status
     */
    private Integer status;

    private Long createTimeBegin;

    private Long createTimeEnd;

    private Long creator;

    private Long updater;

    public StyleCondition() {
        super(StyleSortAttribute.ID.attribute, DESC.identity);
    }

    public StyleCondition(String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
    }

    public StyleCondition(Long id, String nameLike, Integer type, Boolean isActive, Integer status, Long createTimeBegin, Long createTimeEnd,
                          Long creator, Long updater, String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
        this.id = id;
        this.nameLike = nameLike;
        this.type = type;
        this.isActive = isActive;
        this.status = status;
        this.createTimeBegin = createTimeBegin;
        this.createTimeEnd = createTimeEnd;
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

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
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
        return "StyleCondition{" +
                "id=" + id +
                ", nameLike='" + nameLike + '\'' +
                ", type=" + type +
                ", isActive=" + isActive +
                ", status=" + status +
                ", createTimeBegin=" + createTimeBegin +
                ", createTimeEnd=" + createTimeEnd +
                ", creator=" + creator +
                ", updater=" + updater +
                ", sortAttribute='" + sortAttribute + '\'' +
                ", sortType='" + sortType + '\'' +
                '}';
    }

}

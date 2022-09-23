package com.blue.portal.model;

import com.blue.basic.model.common.SortCondition;
import com.blue.portal.constant.NoticeSortAttribute;

import java.io.Serializable;

import static com.blue.basic.constant.common.SortType.DESC;

/**
 * notice condition for select
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class NoticeCondition extends SortCondition implements Serializable {

    private static final long serialVersionUID = 5306796587389624727L;

    private Long id;

    private String titleLike;

    private String linkLike;

    /**
     * @see com.blue.basic.constant.portal.NoticeType
     */
    private Integer type;

    private Long createTimeBegin;

    private Long createTimeEnd;

    private Long updateTimeBegin;

    private Long updateTimeEnd;

    private Long creator;

    private Long updater;

    public NoticeCondition() {
        super(NoticeSortAttribute.ID.attribute, DESC.identity);
    }

    public NoticeCondition(String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
    }

    public NoticeCondition(String sortAttribute, String sortType, Long id, String titleLike, String linkLike, Integer type, Long createTimeBegin, Long createTimeEnd, Long updateTimeBegin, Long updateTimeEnd, Long creator, Long updater) {
        super(sortAttribute, sortType);
        this.id = id;
        this.titleLike = titleLike;
        this.linkLike = linkLike;
        this.type = type;
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

    public String getTitleLike() {
        return titleLike;
    }

    public void setTitleLike(String titleLike) {
        this.titleLike = titleLike;
    }

    public String getLinkLike() {
        return linkLike;
    }

    public void setLinkLike(String linkLike) {
        this.linkLike = linkLike;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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
        return "NoticeCondition{" +
                "id=" + id +
                ", titleLike='" + titleLike + '\'' +
                ", linkLike='" + linkLike + '\'' +
                ", type=" + type +
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

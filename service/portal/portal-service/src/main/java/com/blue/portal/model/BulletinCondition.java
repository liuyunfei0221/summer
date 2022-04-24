package com.blue.portal.model;

import com.blue.base.constant.base.SortType;
import com.blue.base.model.base.SortCondition;
import com.blue.portal.constant.BulletinSortAttribute;

import java.io.Serializable;

/**
 * bulletin condition for select
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class BulletinCondition extends SortCondition implements Serializable {

    private static final long serialVersionUID = 5306796587389624727L;

    private Long id;

    private String title;

    private String link;

    /**
     * @see com.blue.base.constant.portal.BulletinType
     */
    private Integer type;

    /**
     * @see com.blue.base.constant.base.Status
     */
    private Integer status;

    private Long activeTimeBegin;

    private Long activeTimeEnd;

    private Long expireTimeBegin;

    private Long expireTimeEnd;

    private Long createTimeBegin;

    private Long createTimeEnd;

    private Long creator;

    public BulletinCondition() {
        super(BulletinSortAttribute.ID.attribute, SortType.DESC.identity);
    }

    public BulletinCondition(String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
    }

    public BulletinCondition(String sortAttribute, String sortType, Long id, String title, String link, Integer type, Integer status,
                             Long activeTimeBegin, Long activeTimeEnd, Long expireTimeBegin, Long expireTimeEnd, Long createTimeBegin, Long createTimeEnd, Long creator) {
        super(sortAttribute, sortType);
        this.id = id;
        this.title = title;
        this.link = link;
        this.type = type;
        this.status = status;
        this.activeTimeBegin = activeTimeBegin;
        this.activeTimeEnd = activeTimeEnd;
        this.expireTimeBegin = expireTimeBegin;
        this.expireTimeEnd = expireTimeEnd;
        this.createTimeBegin = createTimeBegin;
        this.createTimeEnd = createTimeEnd;
        this.creator = creator;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Long getActiveTimeBegin() {
        return activeTimeBegin;
    }

    public void setActiveTimeBegin(Long activeTimeBegin) {
        this.activeTimeBegin = activeTimeBegin;
    }

    public Long getActiveTimeEnd() {
        return activeTimeEnd;
    }

    public void setActiveTimeEnd(Long activeTimeEnd) {
        this.activeTimeEnd = activeTimeEnd;
    }

    public Long getExpireTimeBegin() {
        return expireTimeBegin;
    }

    public void setExpireTimeBegin(Long expireTimeBegin) {
        this.expireTimeBegin = expireTimeBegin;
    }

    public Long getExpireTimeEnd() {
        return expireTimeEnd;
    }

    public void setExpireTimeEnd(Long expireTimeEnd) {
        this.expireTimeEnd = expireTimeEnd;
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

    @Override
    public String toString() {
        return "BulletinCondition{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", activeTimeBegin=" + activeTimeBegin +
                ", activeTimeEnd=" + activeTimeEnd +
                ", expireTimeBegin=" + expireTimeBegin +
                ", expireTimeEnd=" + expireTimeEnd +
                ", createTimeBegin=" + createTimeBegin +
                ", createTimeEnd=" + createTimeEnd +
                ", creator=" + creator +
                '}';
    }

}

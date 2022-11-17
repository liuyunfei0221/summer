package com.blue.media.model;

import com.blue.basic.model.common.SortCondition;
import com.blue.media.constant.MessageTemplateSortAttribute;

import java.io.Serializable;

import static com.blue.basic.constant.common.SortType.DESC;

/**
 * message template condition for select
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class MessageTemplateCondition extends SortCondition implements Serializable {

    private static final long serialVersionUID = 6934688130621363767L;

    private Long id;

    private String nameLike;

    /**
     * @see com.blue.basic.constant.media.MessageType
     */
    private Integer type;

    /**
     * @see com.blue.basic.constant.media.MessageBusinessType
     */
    private Integer businessType;

    private String titleLike;

    private Long createTimeBegin;

    private Long createTimeEnd;

    private Long updateTimeBegin;

    private Long updateTimeEnd;

    private Long creator;

    private Long updater;

    public MessageTemplateCondition() {
        super(MessageTemplateSortAttribute.CREATE_TIME.attribute, DESC.identity);
    }

    public MessageTemplateCondition(String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
    }

    public MessageTemplateCondition(Long id, String nameLike, Integer type, Integer businessType, String titleLike, Long createTimeBegin, Long createTimeEnd,
                                    Long updateTimeBegin, Long updateTimeEnd, Long creator, Long updater, String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
        this.id = id;
        this.nameLike = nameLike;
        this.type = type;
        this.businessType = businessType;
        this.titleLike = titleLike;
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

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public String getTitleLike() {
        return titleLike;
    }

    public void setTitleLike(String titleLike) {
        this.titleLike = titleLike;
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
        return "MessageTemplateCondition{" +
                "id=" + id +
                ", nameLike='" + nameLike + '\'' +
                ", type=" + type +
                ", businessType=" + businessType +
                ", titleLike='" + titleLike + '\'' +
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

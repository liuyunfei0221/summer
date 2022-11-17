package com.blue.verify.model;

import com.blue.basic.model.common.SortCondition;
import com.blue.verify.constant.VerifyTemplateSortAttribute;

import java.io.Serializable;

import static com.blue.basic.constant.common.SortType.DESC;

/**
 * verify template condition for select
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class VerifyTemplateCondition extends SortCondition implements Serializable {

    private static final long serialVersionUID = 9161547199595249727L;

    private Long id;

    private String nameLike;

    private String descriptionLike;

    /**
     * @see com.blue.basic.constant.verify.VerifyType
     */
    private String type;

    /**
     * @see com.blue.basic.constant.verify.VerifyBusinessType
     */
    private String businessType;

    private String language;

    private String titleLike;

    private String contentLike;

    private Long createTimeBegin;

    private Long createTimeEnd;

    private Long updateTimeBegin;

    private Long updateTimeEnd;

    private Long creator;

    private Long updater;

    public VerifyTemplateCondition() {
        super(VerifyTemplateSortAttribute.CREATE_TIME.attribute, DESC.identity);
    }

    public VerifyTemplateCondition(String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
    }

    public VerifyTemplateCondition(Long id, String nameLike, String descriptionLike, String type, String businessType, String language, String titleLike, String contentLike,
                                   Long createTimeBegin, Long createTimeEnd, Long updateTimeBegin, Long updateTimeEnd, Long creator, Long updater, String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
        this.id = id;
        this.nameLike = nameLike;
        this.descriptionLike = descriptionLike;
        this.type = type;
        this.businessType = businessType;
        this.language = language;
        this.titleLike = titleLike;
        this.contentLike = contentLike;
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

    public String getDescriptionLike() {
        return descriptionLike;
    }

    public void setDescriptionLike(String descriptionLike) {
        this.descriptionLike = descriptionLike;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTitleLike() {
        return titleLike;
    }

    public void setTitleLike(String titleLike) {
        this.titleLike = titleLike;
    }

    public String getContentLike() {
        return contentLike;
    }

    public void setContentLike(String contentLike) {
        this.contentLike = contentLike;
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
        return "VerifyTemplateCondition{" +
                "id=" + id +
                ", nameLike='" + nameLike + '\'' +
                ", descriptionLike='" + descriptionLike + '\'' +
                ", type='" + type + '\'' +
                ", businessType='" + businessType + '\'' +
                ", language='" + language + '\'' +
                ", titleLike='" + titleLike + '\'' +
                ", contentLike='" + contentLike + '\'' +
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

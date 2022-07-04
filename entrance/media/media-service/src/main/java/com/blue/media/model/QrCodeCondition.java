package com.blue.media.model;

import com.blue.base.model.common.SortCondition;
import com.blue.media.constant.QrCodeConfigSortAttribute;

import java.io.Serializable;

import static com.blue.base.constant.common.SortType.DESC;

/**
 * qr code config condition for select
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class QrCodeCondition extends SortCondition implements Serializable {

    private static final long serialVersionUID = 1765844272453476492L;

    private Long id;

    private String nameLike;

    private String descriptionLike;

    /**
     * unique qr code type
     */
    private Integer type;

    /**
     * @see com.blue.base.constant.media.QrCodeGenType
     */
    private Integer genHandlerType;

    private String domainLike;

    private String pathToBeFilledLike;

    private Integer placeholderCount;

    private Integer status;

    private Long createTimeBegin;

    private Long createTimeEnd;

    private Long updateTimeBegin;

    private Long updateTimeEnd;

    private Long creator;

    private Long updater;

    public QrCodeCondition() {
        super(QrCodeConfigSortAttribute.ID.attribute, DESC.identity);
    }

    public QrCodeCondition(String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
    }

    public QrCodeCondition(Long id, String nameLike, String descriptionLike, Integer type, Integer genHandlerType,
                           String domainLike, String pathToBeFilledLike, Integer placeholderCount, Integer status,
                           Long createTimeBegin, Long createTimeEnd, Long updateTimeBegin, Long updateTimeEnd, Long creator, Long updater,
                           String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
        this.id = id;
        this.nameLike = nameLike;
        this.descriptionLike = descriptionLike;
        this.type = type;
        this.genHandlerType = genHandlerType;
        this.domainLike = domainLike;
        this.pathToBeFilledLike = pathToBeFilledLike;
        this.placeholderCount = placeholderCount;
        this.status = status;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getGenHandlerType() {
        return genHandlerType;
    }

    public void setGenHandlerType(Integer genHandlerType) {
        this.genHandlerType = genHandlerType;
    }

    public String getDomainLike() {
        return domainLike;
    }

    public void setDomainLike(String domainLike) {
        this.domainLike = domainLike;
    }

    public String getPathToBeFilledLike() {
        return pathToBeFilledLike;
    }

    public void setPathToBeFilledLike(String pathToBeFilledLike) {
        this.pathToBeFilledLike = pathToBeFilledLike;
    }

    public Integer getPlaceholderCount() {
        return placeholderCount;
    }

    public void setPlaceholderCount(Integer placeholderCount) {
        this.placeholderCount = placeholderCount;
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
        return "QrCodeCondition{" +
                "id=" + id +
                ", nameLike='" + nameLike + '\'' +
                ", descriptionLike='" + descriptionLike + '\'' +
                ", type=" + type +
                ", genHandlerType=" + genHandlerType +
                ", domainLike='" + domainLike + '\'' +
                ", pathToBeFilledLike='" + pathToBeFilledLike + '\'' +
                ", placeholderCount=" + placeholderCount +
                ", status=" + status +
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

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

    private String name;

    /**
     * qr code type
     *
     * @see com.blue.base.constant.media.QrCodeType
     */
    private Integer type;

    private String domain;

    private String pathToBeFilled;

    private Integer placeholderCount;

    private String fileType;

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

    public QrCodeCondition(Long id, String name, Integer type, String domain, String pathToBeFilled, Integer placeholderCount, String fileType, Integer status,
                           Long createTimeBegin, Long createTimeEnd, Long updateTimeBegin, Long updateTimeEnd, Long creator, Long updater, String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
        this.id = id;
        this.name = name;
        this.type = type;
        this.domain = domain;
        this.pathToBeFilled = pathToBeFilled;
        this.placeholderCount = placeholderCount;
        this.fileType = fileType;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPathToBeFilled() {
        return pathToBeFilled;
    }

    public void setPathToBeFilled(String pathToBeFilled) {
        this.pathToBeFilled = pathToBeFilled;
    }

    public Integer getPlaceholderCount() {
        return placeholderCount;
    }

    public void setPlaceholderCount(Integer placeholderCount) {
        this.placeholderCount = placeholderCount;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
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
                ", name='" + name + '\'' +
                ", type=" + type +
                ", domain='" + domain + '\'' +
                ", pathToBeFilled='" + pathToBeFilled + '\'' +
                ", placeholderCount=" + placeholderCount +
                ", fileType='" + fileType + '\'' +
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

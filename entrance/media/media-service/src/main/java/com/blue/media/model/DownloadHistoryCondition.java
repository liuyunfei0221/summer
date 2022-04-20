package com.blue.media.model;

import java.io.Serializable;

/**
 * download history condition for select
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class DownloadHistoryCondition implements Serializable {

    private static final long serialVersionUID = 5402945303876982613L;

    private Long id;

    private Long attachmentId;

    private Long creator;

    private Long createTimeBegin;

    private Long createTimeEnd;

    private String sortAttribute;

    private String sortType;

    public DownloadHistoryCondition() {
    }

    public DownloadHistoryCondition(Long id, Long attachmentId, Long creator, Long createTimeBegin, Long createTimeEnd, String sortAttribute, String sortType) {
        this.id = id;
        this.attachmentId = attachmentId;
        this.creator = creator;
        this.createTimeBegin = createTimeBegin;
        this.createTimeEnd = createTimeEnd;
        this.sortAttribute = sortAttribute;
        this.sortType = sortType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(Long attachmentId) {
        this.attachmentId = attachmentId;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
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

    public String getSortAttribute() {
        return sortAttribute;
    }

    public void setSortAttribute(String sortAttribute) {
        this.sortAttribute = sortAttribute;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    @Override
    public String toString() {
        return "DownloadHistoryCondition{" +
                "id=" + id +
                ", attachmentId=" + attachmentId +
                ", creator=" + creator +
                ", createTimeBegin=" + createTimeBegin +
                ", createTimeEnd=" + createTimeEnd +
                ", sortAttribute='" + sortAttribute + '\'' +
                ", sortType='" + sortType + '\'' +
                '}';
    }

}

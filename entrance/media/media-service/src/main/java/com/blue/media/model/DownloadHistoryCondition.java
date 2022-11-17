package com.blue.media.model;

import com.blue.basic.constant.common.SortType;
import com.blue.basic.model.common.SortCondition;
import com.blue.media.constant.DownloadHistorySortAttribute;

import java.io.Serializable;

/**
 * download history condition for select
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class DownloadHistoryCondition extends SortCondition implements Serializable {

    private static final long serialVersionUID = 5402945303876982613L;

    private Long id;

    private Long attachmentId;

    private Long creator;

    private Long createTimeBegin;

    private Long createTimeEnd;

    public DownloadHistoryCondition() {
        super(DownloadHistorySortAttribute.CREATE_TIME.attribute, SortType.DESC.identity);
    }

    public DownloadHistoryCondition(String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
    }

    public DownloadHistoryCondition(Long id, Long attachmentId, Long creator, Long createTimeBegin, Long createTimeEnd, String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
        this.id = id;
        this.attachmentId = attachmentId;
        this.creator = creator;
        this.createTimeBegin = createTimeBegin;
        this.createTimeEnd = createTimeEnd;
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

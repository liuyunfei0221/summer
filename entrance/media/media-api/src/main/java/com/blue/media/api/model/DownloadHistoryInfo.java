package com.blue.media.api.model;

import java.io.Serializable;

/**
 * download history info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class DownloadHistoryInfo implements Serializable {

    private static final long serialVersionUID = -4800094825903540939L;

    private final Long id;

    private final Long attachmentId;

    private final String attachmentName;

    private final Long createTime;

    private final Long creator;

    private final String creatorName;

    public DownloadHistoryInfo(Long id, Long attachmentId, String attachmentName, Long createTime, Long creator, String creatorName) {
        this.id = id;
        this.attachmentId = attachmentId;
        this.attachmentName = attachmentName;
        this.createTime = createTime;
        this.creator = creator;
        this.creatorName = creatorName;
    }

    public Long getId() {
        return id;
    }

    public Long getAttachmentId() {
        return attachmentId;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public Long getCreator() {
        return creator;
    }

    public String getCreatorName() {
        return creatorName;
    }

    @Override
    public String toString() {
        return "DownloadHistoryInfo{" +
                "id=" + id +
                ", attachmentId=" + attachmentId +
                ", attachmentName='" + attachmentName + '\'' +
                ", createTime=" + createTime +
                ", creator=" + creator +
                ", creatorName='" + creatorName + '\'' +
                '}';
    }

}
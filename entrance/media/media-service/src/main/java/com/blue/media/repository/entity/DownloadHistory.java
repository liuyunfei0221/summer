package com.blue.media.repository.entity;

import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * download history
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class DownloadHistory implements Serializable {

    private static final long serialVersionUID = -1824231125493320466L;

    @Id
    private Long id;

    private Long attachmentId;

    private Long createTime;

    private Long creator;

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

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    @Override
    public String toString() {
        return "DownloadHistory{" +
                "id=" + id +
                ", attachmentId=" + attachmentId +
                ", createTime=" + createTime +
                ", creator=" + creator +
                '}';
    }

}
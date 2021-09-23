package com.blue.file.repository.entity;

/**
 * 下载历史表
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class DownloadHistory {

    /**
     * 主键
     */
    private Long id;

    /**
     * 文件id
     */
    private Long attachmentId;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 创建人
     */
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

}
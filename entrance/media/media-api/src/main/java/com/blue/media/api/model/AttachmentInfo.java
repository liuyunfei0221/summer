package com.blue.media.api.model;

import java.io.Serializable;

/**
 * attachment detail info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class AttachmentInfo implements Serializable {

    private static final long serialVersionUID = -2972330057905370045L;

    private final Long id;

    private final String name;

    private final Long size;

    private final Integer status;

    private final Long createTime;

    private final Long creator;

    private final String creatorName;

    public AttachmentInfo(Long id, String name, Long size, Integer status, Long createTime, Long creator, String creatorName) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.status = status;
        this.createTime = createTime;
        this.creator = creator;
        this.creatorName = creatorName;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getSize() {
        return size;
    }

    public Integer getStatus() {
        return status;
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
        return "AttachmentInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", size=" + size +
                ", status=" + status +
                ", createTime=" + createTime +
                ", creator=" + creator +
                ", creatorName='" + creatorName + '\'' +
                '}';
    }

}

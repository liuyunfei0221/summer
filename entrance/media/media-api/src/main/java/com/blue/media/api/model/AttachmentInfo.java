package com.blue.media.api.model;

import java.io.Serializable;

/**
 * attachment info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class AttachmentInfo implements Serializable {

    private static final long serialVersionUID = -2972330057905370045L;

    private final Long id;

    private final String name;

    private final Long size;

    private final Long createTime;

    private final String creatorName;

    public AttachmentInfo(Long id, String name, Long size, Long createTime, String creatorName) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.createTime = createTime;
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

    public Long getCreateTime() {
        return createTime;
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
                ", createTime=" + createTime +
                ", creatorName='" + creatorName + '\'' +
                '}';
    }

}

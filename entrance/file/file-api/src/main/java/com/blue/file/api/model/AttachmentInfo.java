package com.blue.file.api.model;

import java.io.Serializable;

/**
 * 附件信息封装类
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class AttachmentInfo implements Serializable {

    private static final long serialVersionUID = -2972330057905370045L;

    /**
     * 附件id
     */
    private final Long id;

    /**
     * 附件名称
     */
    private final String name;

    /**
     * 附件大小
     */
    private final Long size;

    /**
     * 创建时间
     */
    private final Long createTime;

    /**
     * 创建人
     */
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
        return "AttachmentVO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", size=" + size +
                ", createTime=" + createTime +
                ", createrName='" + creatorName + '\'' +
                '}';
    }
}

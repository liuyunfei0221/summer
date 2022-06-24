package com.blue.media.api.model;

import java.io.Serializable;

/**
 * attachment detail info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class AttachmentDetailInfo implements Serializable {

    private static final long serialVersionUID = -2972330057905370045L;

    private Long id;

    /**
     * attachment type
     *
     * @see com.blue.base.constant.media.AttachmentType
     */
    private Integer type;

    private String link;

    private String name;

    private Long size;

    private Integer status;

    private Long createTime;

    private Long creator;

    private String creatorName;

    public AttachmentDetailInfo() {
    }

    public AttachmentDetailInfo(Long id, Integer type, String link, String name, Long size, Integer status, Long createTime, Long creator, String creatorName) {
        this.id = id;
        this.type = type;
        this.link = link;
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

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    @Override
    public String toString() {
        return "AttachmentDetailInfo{" +
                "id=" + id +
                ", type=" + type +
                ", link='" + link + '\'' +
                ", name='" + name + '\'' +
                ", size=" + size +
                ", status=" + status +
                ", createTime=" + createTime +
                ", creator=" + creator +
                ", creatorName='" + creatorName + '\'' +
                '}';
    }

}

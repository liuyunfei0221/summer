package com.blue.media.api.model;

import com.blue.basic.serializer.Long2StringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * attachment info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class AttachmentInfo implements Serializable {

    private static final long serialVersionUID = -697205574047584331L;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long id;

    /**
     * attachment type
     *
     * @see com.blue.basic.constant.media.AttachmentType
     */
    private Integer type;

    private String link;

    private String name;

    private String fileType;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long size;

    private Integer status;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long createTime;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long creator;

    public AttachmentInfo() {
    }

    public AttachmentInfo(Long id, Integer type, String link, String name, String fileType, Long size, Integer status, Long createTime, Long creator) {
        this.id = id;
        this.type = type;
        this.link = link;
        this.name = name;
        this.fileType = fileType;
        this.size = size;
        this.status = status;
        this.createTime = createTime;
        this.creator = creator;
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

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
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

    @Override
    public String toString() {
        return "AttachmentInfo{" +
                "id=" + id +
                ", type=" + type +
                ", link='" + link + '\'' +
                ", name='" + name + '\'' +
                ", fileType='" + fileType + '\'' +
                ", size=" + size +
                ", status=" + status +
                ", createTime=" + createTime +
                ", creator=" + creator +
                '}';
    }

}

package com.blue.media.repository.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isNull;

/**
 * attachment
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
@Document(collection = "attachment")
public final class Attachment implements Serializable {

    private static final long serialVersionUID = -9017169609442683728L;

    @Id
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

    private Long size;

    private Integer status;

    private Long createTime;

    private Long creator;

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
        this.link = isNull(link) ? null : link.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = isNull(name) ? null : name.trim();
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
        return "Attachment{" +
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
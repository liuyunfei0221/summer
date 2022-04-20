package com.blue.media.model;

import java.io.Serializable;

/**
 * attachment condition for select
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class AttachmentCondition implements Serializable {

    private static final long serialVersionUID = 1899006299426322890L;

    private Long id;

    private String link;

    private String name;

    private String fileType;

    private Long createTimeBegin;

    private Long createTimeEnd;

    private String sortAttribute;

    private String sortType;

    public AttachmentCondition() {
    }

    public AttachmentCondition(Long id, String link, String name, String fileType, Long createTimeBegin, Long createTimeEnd, String sortAttribute, String sortType) {
        this.id = id;
        this.link = link;
        this.name = name;
        this.fileType = fileType;
        this.createTimeBegin = createTimeBegin;
        this.createTimeEnd = createTimeEnd;
        this.sortAttribute = sortAttribute;
        this.sortType = sortType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getSortAttribute() {
        return sortAttribute;
    }

    public void setSortAttribute(String sortAttribute) {
        this.sortAttribute = sortAttribute;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    @Override
    public String toString() {
        return "AttachmentCondition{" +
                "id=" + id +
                ", link='" + link + '\'' +
                ", name='" + name + '\'' +
                ", fileType='" + fileType + '\'' +
                ", createTimeBegin=" + createTimeBegin +
                ", createTimeEnd=" + createTimeEnd +
                ", sortAttribute='" + sortAttribute + '\'' +
                ", sortType='" + sortType + '\'' +
                '}';
    }

}

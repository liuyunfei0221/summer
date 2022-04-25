package com.blue.media.model;

import com.blue.base.model.base.SortCondition;
import com.blue.media.constant.AttachmentSortAttribute;

import java.io.Serializable;

import static com.blue.base.constant.base.SortType.DESC;

/**
 * attachment condition for select
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class AttachmentCondition extends SortCondition implements Serializable {

    private static final long serialVersionUID = 1899006299426322890L;

    private Long id;

    private String linkLike;

    private String nameLike;

    private String fileType;

    private Long createTimeBegin;

    private Long createTimeEnd;

    public AttachmentCondition() {
        super(AttachmentSortAttribute.ID.attribute, DESC.identity);
    }

    public AttachmentCondition(String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
    }

    public AttachmentCondition(Long id, String linkLike, String nameLike, String fileType, Long createTimeBegin, Long createTimeEnd, String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
        this.id = id;
        this.linkLike = linkLike;
        this.nameLike = nameLike;
        this.fileType = fileType;
        this.createTimeBegin = createTimeBegin;
        this.createTimeEnd = createTimeEnd;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLinkLike() {
        return linkLike;
    }

    public void setLinkLike(String linkLike) {
        this.linkLike = linkLike;
    }

    public String getNameLike() {
        return nameLike;
    }

    public void setNameLike(String nameLike) {
        this.nameLike = nameLike;
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

    @Override
    public String toString() {
        return "AttachmentCondition{" +
                "id=" + id +
                ", linkLike='" + linkLike + '\'' +
                ", nameLike='" + nameLike + '\'' +
                ", fileType='" + fileType + '\'' +
                ", createTimeBegin=" + createTimeBegin +
                ", createTimeEnd=" + createTimeEnd +
                ", sortAttribute='" + sortAttribute + '\'' +
                ", sortType='" + sortType + '\'' +
                '}';
    }

}

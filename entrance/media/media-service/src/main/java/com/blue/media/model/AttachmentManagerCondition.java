package com.blue.media.model;

import com.blue.basic.model.common.SortCondition;
import com.blue.media.constant.AttachmentSortAttribute;

import java.io.Serializable;

import static com.blue.basic.constant.common.SortType.DESC;

/**
 * attachment manager condition for select
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class AttachmentManagerCondition extends SortCondition implements Serializable {

    private static final long serialVersionUID = 1899006299426322890L;

    private Long id;

    private String linkLike;

    private String nameLike;

    private String fileType;

    private Long createTimeBegin;

    private Long createTimeEnd;

    private Long creator;

    public AttachmentManagerCondition() {
        super(AttachmentSortAttribute.ID.attribute, DESC.identity);
    }

    public AttachmentManagerCondition(String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
    }

    public AttachmentManagerCondition(Long id, String linkLike, String nameLike, String fileType, Long createTimeBegin, Long createTimeEnd, Long creator, String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
        this.id = id;
        this.linkLike = linkLike;
        this.nameLike = nameLike;
        this.fileType = fileType;
        this.createTimeBegin = createTimeBegin;
        this.createTimeEnd = createTimeEnd;
        this.creator = creator;
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

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    @Override
    public String toString() {
        return "AttachmentManagerCondition{" +
                "id=" + id +
                ", linkLike='" + linkLike + '\'' +
                ", nameLike='" + nameLike + '\'' +
                ", fileType='" + fileType + '\'' +
                ", createTimeBegin=" + createTimeBegin +
                ", createTimeEnd=" + createTimeEnd +
                ", creator=" + creator +
                ", sortAttribute='" + sortAttribute + '\'' +
                ", sortType='" + sortType + '\'' +
                '}';
    }

}

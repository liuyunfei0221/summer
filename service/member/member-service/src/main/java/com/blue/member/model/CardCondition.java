package com.blue.member.model;

import com.blue.basic.constant.common.SortType;
import com.blue.basic.model.common.SortCondition;
import com.blue.member.constant.CardSortAttribute;

import java.io.Serializable;

/**
 * card condition for select
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class CardCondition extends SortCondition implements Serializable {

    private static final long serialVersionUID = -821487725449043722L;

    private Long id;

    private Long memberId;

    private String nameLike;

    private String detailLike;

    private Long coverId;

    private String coverLinkLike;

    private Long contentId;

    private String contentLinkLike;

    /**
     * @see com.blue.basic.constant.common.Status
     */
    private Integer status;

    private Long createTimeBegin;

    private Long createTimeEnd;

    private Long updateTimeBegin;

    private Long updateTimeEnd;

    public CardCondition() {
        super(CardSortAttribute.CREATE_TIME.attribute, SortType.DESC.identity);
    }

    public CardCondition(String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
    }

    public CardCondition(Long id, Long memberId, String nameLike, String detailLike,
                         Long coverId, String coverLinkLike, Long contentId, String contentLinkLike,
                         Integer status, Long createTimeBegin, Long createTimeEnd, Long updateTimeBegin, Long updateTimeEnd,
                         String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
        this.id = id;
        this.memberId = memberId;
        this.nameLike = nameLike;
        this.detailLike = detailLike;
        this.coverId = coverId;
        this.coverLinkLike = coverLinkLike;
        this.contentId = contentId;
        this.contentLinkLike = contentLinkLike;
        this.status = status;
        this.createTimeBegin = createTimeBegin;
        this.createTimeEnd = createTimeEnd;
        this.updateTimeBegin = updateTimeBegin;
        this.updateTimeEnd = updateTimeEnd;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getNameLike() {
        return nameLike;
    }

    public void setNameLike(String nameLike) {
        this.nameLike = nameLike;
    }

    public String getDetailLike() {
        return detailLike;
    }

    public void setDetailLike(String detailLike) {
        this.detailLike = detailLike;
    }

    public Long getCoverId() {
        return coverId;
    }

    public void setCoverId(Long coverId) {
        this.coverId = coverId;
    }

    public String getCoverLinkLike() {
        return coverLinkLike;
    }

    public void setCoverLinkLike(String coverLinkLike) {
        this.coverLinkLike = coverLinkLike;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public String getContentLinkLike() {
        return contentLinkLike;
    }

    public void setContentLinkLike(String contentLinkLike) {
        this.contentLinkLike = contentLinkLike;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Long getUpdateTimeBegin() {
        return updateTimeBegin;
    }

    public void setUpdateTimeBegin(Long updateTimeBegin) {
        this.updateTimeBegin = updateTimeBegin;
    }

    public Long getUpdateTimeEnd() {
        return updateTimeEnd;
    }

    public void setUpdateTimeEnd(Long updateTimeEnd) {
        this.updateTimeEnd = updateTimeEnd;
    }

    @Override
    public String toString() {
        return "CardCondition{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", nameLike='" + nameLike + '\'' +
                ", detailLike='" + detailLike + '\'' +
                ", coverId=" + coverId +
                ", coverLinkLike='" + coverLinkLike + '\'' +
                ", contentId=" + contentId +
                ", contentLinkLike='" + contentLinkLike + '\'' +
                ", status=" + status +
                ", createTimeBegin=" + createTimeBegin +
                ", createTimeEnd=" + createTimeEnd +
                ", updateTimeBegin=" + updateTimeBegin +
                ", updateTimeEnd=" + updateTimeEnd +
                ", sortAttribute='" + sortAttribute + '\'' +
                ", sortType='" + sortType + '\'' +
                '}';
    }
}

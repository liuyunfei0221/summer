package com.blue.member.api.model;

import java.io.Serializable;

/**
 * card info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class CardInfo implements Serializable {

    private static final long serialVersionUID = 8508950545229880917L;

    private Long id;

    private Long memberId;

    private String name;

    private String detail;

    /**
     * cover attachment id
     */
    private Long coverId;

    /**
     * cover attachment link
     */
    private String coverLink;

    /**
     * content attachment id
     */
    private Long contentId;

    /**
     * content attachment link
     */
    private String contentLink;

    private String extra;

    private Long createTime;

    private Long updateTime;

    public CardInfo() {
    }

    public CardInfo(Long id, Long memberId, String name, String detail, Long coverId, String coverLink, Long contentId, String contentLink, String extra, Long createTime, Long updateTime) {
        this.id = id;
        this.memberId = memberId;
        this.name = name;
        this.detail = detail;
        this.coverId = coverId;
        this.coverLink = coverLink;
        this.contentId = contentId;
        this.contentLink = contentLink;
        this.extra = extra;
        this.createTime = createTime;
        this.updateTime = updateTime;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Long getCoverId() {
        return coverId;
    }

    public void setCoverId(Long coverId) {
        this.coverId = coverId;
    }

    public String getCoverLink() {
        return coverLink;
    }

    public void setCoverLink(String coverLink) {
        this.coverLink = coverLink;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public String getContentLink() {
        return contentLink;
    }

    public void setContentLink(String contentLink) {
        this.contentLink = contentLink;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "CardInfo{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", name='" + name + '\'' +
                ", detail='" + detail + '\'' +
                ", coverId=" + coverId +
                ", coverLink='" + coverLink + '\'' +
                ", contentId=" + contentId +
                ", contentLink='" + contentLink + '\'' +
                ", extra='" + extra + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

}

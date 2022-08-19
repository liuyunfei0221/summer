package com.blue.member.api.model;

import com.blue.basic.serializer.IdentityDeserializer;
import com.blue.basic.serializer.IdentitySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * card detail info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class CardDetailInfo implements Serializable {

    private static final long serialVersionUID = -4667279788771456871L;

    @JsonSerialize(using = IdentitySerializer.class)
    @JsonDeserialize(using = IdentityDeserializer.class)
    private Long id;

    @JsonSerialize(using = IdentitySerializer.class)
    @JsonDeserialize(using = IdentityDeserializer.class)
    private Long memberId;

    private String name;

    private String detail;

    /**
     * cover attachment id
     */
    @JsonSerialize(using = IdentitySerializer.class)
    @JsonDeserialize(using = IdentityDeserializer.class)
    private Long coverId;

    /**
     * cover attachment link
     */
    private String coverLink;

    /**
     * content attachment id
     */
    @JsonSerialize(using = IdentitySerializer.class)
    @JsonDeserialize(using = IdentityDeserializer.class)
    private Long contentId;

    /**
     * content attachment link
     */
    private String contentLink;

    private String extra;

    @JsonSerialize(using = IdentitySerializer.class)
    @JsonDeserialize(using = IdentityDeserializer.class)
    private Long createTime;

    @JsonSerialize(using = IdentitySerializer.class)
    @JsonDeserialize(using = IdentityDeserializer.class)
    private Long updateTime;

    private String creatorName;

    public CardDetailInfo() {
    }

    public CardDetailInfo(Long id, Long memberId, String name, String detail, Long coverId, String coverLink, Long contentId, String contentLink, String extra, Long createTime, Long updateTime, String creatorName) {
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
        this.creatorName = creatorName;
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

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    @Override
    public String toString() {
        return "CardDetailInfo{" +
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
                ", creatorName='" + creatorName + '\'' +
                '}';
    }

}

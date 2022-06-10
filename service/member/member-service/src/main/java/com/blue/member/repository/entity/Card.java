package com.blue.member.repository.entity;

import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * card entity
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public class Card implements Serializable {

    private static final long serialVersionUID = -2386167098351023193L;
    
    @Id
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

    private Integer status;

    private Long createTime;

    private Long updateTime;

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

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", name='" + name + '\'' +
                ", detail='" + detail + '\'' +
                ", coverId=" + coverId +
                ", coverLink='" + coverLink + '\'' +
                ", contentId=" + contentId +
                ", contentLink='" + contentLink + '\'' +
                ", extra='" + extra + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

}

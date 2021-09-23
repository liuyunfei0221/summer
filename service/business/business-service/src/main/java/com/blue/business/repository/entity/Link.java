package com.blue.business.repository.entity;

public class Link {

    private Long id;

    private Long subId;

    private Integer subType;

    private Long subAuthorId;

    private String linkUrl;

    private String content;

    private Long favorites;

    private Long readings;

    private Long comments;

    private Long likes;

    private Long boring;

    private Integer status;

    private Long createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSubId() {
        return subId;
    }

    public void setSubId(Long subId) {
        this.subId = subId;
    }

    public Integer getSubType() {
        return subType;
    }

    public void setSubType(Integer subType) {
        this.subType = subType;
    }

    public Long getSubAuthorId() {
        return subAuthorId;
    }

    public void setSubAuthorId(Long subAuthorId) {
        this.subAuthorId = subAuthorId;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl == null ? null : linkUrl.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Long getFavorites() {
        return favorites;
    }

    public void setFavorites(Long favorites) {
        this.favorites = favorites;
    }

    public Long getReadings() {
        return readings;
    }

    public void setReadings(Long readings) {
        this.readings = readings;
    }

    public Long getComments() {
        return comments;
    }

    public void setComments(Long comments) {
        this.comments = comments;
    }

    public Long getLikes() {
        return likes;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }

    public Long getBoring() {
        return boring;
    }

    public void setBoring(Long boring) {
        this.boring = boring;
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
}
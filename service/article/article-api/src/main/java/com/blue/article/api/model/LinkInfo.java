package com.blue.article.api.model;

import com.blue.basic.serializer.Long2StringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * link info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class LinkInfo implements Serializable {

    private static final long serialVersionUID = -1399542570751999295L;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long id;

    private String linkUrl;

    private String content;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long favorites;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long readings;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long comments;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long likes;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long boring;

    public LinkInfo() {
    }

    public LinkInfo(Long id, String linkUrl, String content, Long favorites,
                    Long readings, Long comments, Long likes, Long boring) {
        this.id = id;
        this.linkUrl = linkUrl;
        this.content = content;
        this.favorites = favorites;
        this.readings = readings;
        this.comments = comments;
        this.likes = likes;
        this.boring = boring;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    @Override
    public String toString() {
        return "LinkInfo{" +
                "id=" + id +
                ", linkUrl='" + linkUrl + '\'' +
                ", content='" + content + '\'' +
                ", favorites=" + favorites +
                ", readings=" + readings +
                ", comments=" + comments +
                ", likes=" + likes +
                ", boring=" + boring +
                '}';
    }

}

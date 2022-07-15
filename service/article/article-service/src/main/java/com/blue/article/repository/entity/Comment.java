package com.blue.article.repository.entity;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isNull;

/**
 * comment entity
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class Comment implements Serializable {

    private static final long serialVersionUID = 7089856147035171817L;

    private Long id;

    private Long subId;

    private Byte subType;

    private Long subAuthorId;

    private Long fromId;

    private Long favorites;

    private Long replies;

    private Long likes;

    private Long boring;

    private Byte status;

    private Long createTime;

    private String content;

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

    public Byte getSubType() {
        return subType;
    }

    public void setSubType(Byte subType) {
        this.subType = subType;
    }

    public Long getSubAuthorId() {
        return subAuthorId;
    }

    public void setSubAuthorId(Long subAuthorId) {
        this.subAuthorId = subAuthorId;
    }

    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public Long getFavorites() {
        return favorites;
    }

    public void setFavorites(Long favorites) {
        this.favorites = favorites;
    }

    public Long getReplies() {
        return replies;
    }

    public void setReplies(Long replies) {
        this.replies = replies;
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

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = isNull(content) ? null : content.trim();
    }

}
package com.blue.business.api.model;

import java.io.Serializable;
import java.util.List;

/**
 * 文章信息封装
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class ArticleInfo implements Serializable {

    private static final long serialVersionUID = -9167970886461067666L;

    private Long id;

    private String title;

    private Long authorId;

    private String author;

    private Integer type;

    private String typeDisc;

    private Long createTime;

    private Long updateTime;

    private String content;

    private ReadingInfo readingInfo;

    private List<LinkInfo> links;

    public ArticleInfo() {
    }

    public ArticleInfo(Long id, String title, Long authorId, String author, Integer type, String typeDisc,
                       Long createTime, Long updateTime, String content, ReadingInfo readingInfo, List<LinkInfo> links) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
        this.author = author;
        this.type = type;
        this.typeDisc = typeDisc;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.content = content;
        this.readingInfo = readingInfo;
        this.links = links;
    }

    public  Long getId() {
        return id;
    }

    public  void setId(Long id) {
        this.id = id;
    }

    public  String getTitle() {
        return title;
    }

    public  void setTitle(String title) {
        this.title = title;
    }

    public  Long getAuthorId() {
        return authorId;
    }

    public  void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public  String getAuthor() {
        return author;
    }

    public  void setAuthor(String author) {
        this.author = author;
    }

    public  Integer getType() {
        return type;
    }

    public  void setType(Integer type) {
        this.type = type;
    }

    public  String getTypeDisc() {
        return typeDisc;
    }

    public  void setTypeDisc(String typeDisc) {
        this.typeDisc = typeDisc;
    }

    public  Long getCreateTime() {
        return createTime;
    }

    public  void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public  Long getUpdateTime() {
        return updateTime;
    }

    public  void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public  String getContent() {
        return content;
    }

    public  void setContent(String content) {
        this.content = content;
    }

    public  ReadingInfo getReadingVO() {
        return readingInfo;
    }

    public  void setReadingVO(ReadingInfo readingInfo) {
        this.readingInfo = readingInfo;
    }

    public  List<LinkInfo> getLinks() {
        return links;
    }

    public  void setLinks(List<LinkInfo> links) {
        this.links = links;
    }

    @Override
    public  String toString() {
        return "ArticleVO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", authorId=" + authorId +
                ", author='" + author + '\'' +
                ", type=" + type +
                ", typeDisc='" + typeDisc + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", content='" + content + '\'' +
                ", readingVO=" + readingInfo +
                ", links=" + links +
                '}';
    }

}

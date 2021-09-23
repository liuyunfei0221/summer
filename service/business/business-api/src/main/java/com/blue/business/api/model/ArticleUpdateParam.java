package com.blue.business.api.model;

import java.io.Serializable;
import java.util.List;

/**
 * 修改文章信息数据封装
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class ArticleUpdateParam implements Serializable {

    private static final long serialVersionUID = 3538413339258696917L;

    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 作者
     */
    private String author;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 内容
     */
    private String content;

    /**
     * 相关链接
     */
    private List<String> links;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getLinks() {
        return links;
    }

    public void setLinks(List<String> links) {
        this.links = links;
    }

    @Override
    public String toString() {
        return "ArticleUpdateDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", type=" + type +
                ", content='" + content + '\'' +
                ", links=" + links +
                '}';
    }

}

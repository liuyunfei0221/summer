package com.blue.business.api.model;

import java.io.Serializable;
import java.util.List;

/**
 * article insert param
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class ArticleInsertParam implements Serializable {

    private static final long serialVersionUID = 40217575295999511L;

    private String title;

    private Integer type;

    private String content;

    private List<LinkInsertParam> links;

    public ArticleInsertParam() {
    }

    public ArticleInsertParam(String title, Integer type, String content, List<LinkInsertParam> links) {
        this.title = title;
        this.type = type;
        this.content = content;
        this.links = links;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public List<LinkInsertParam> getLinks() {
        return links;
    }

    public void setLinks(List<LinkInsertParam> links) {
        this.links = links;
    }

    @Override
    public String toString() {
        return "ArticleInsertInfo{" +
                "title='" + title + '\'' +
                ", type=" + type +
                ", content='" + content + '\'' +
                ", links=" + links +
                '}';
    }

}

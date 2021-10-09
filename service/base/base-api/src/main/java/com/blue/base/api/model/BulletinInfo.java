package com.blue.base.api.model;

import java.io.Serializable;


/**
 * test model
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class BulletinInfo implements Serializable {

    private static final long serialVersionUID = -2248462404325218989L;

    private Long id;

    private String title;

    private String content;

    private String link;

    private Integer type;

    public BulletinInfo() {
    }

    public BulletinInfo(Long id, String title, String content, String link, Integer type) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.link = link;
        this.type = type;
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "BulletinVO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", link='" + link + '\'' +
                ", type=" + type +
                '}';
    }

}

package com.blue.base.api.model;

import java.io.Serializable;


/**
 * 公告信息封装
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class BulletinInfo implements Serializable {

    private static final long serialVersionUID = -2248462404325218989L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告内容
     */
    private String content;

    /**
     * 公告图片链接
     */
    private String link;

    /**
     * 公告类型 1热门
     */
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

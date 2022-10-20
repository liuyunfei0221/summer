package com.blue.portal.api.model;

import java.io.Serializable;


/**
 * bulletin api info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class BulletinInfo implements Serializable {

    private static final long serialVersionUID = -2248462404325218989L;

    private Long id;

    private String title;

    private String content;

    private String link;

    private Integer type;

    private Integer priority;

    private Long activeTime;

    private Long expireTime;

    public BulletinInfo() {
    }

    public BulletinInfo(Long id, String title, String content, String link, Integer type, Integer priority, Long activeTime, Long expireTime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.link = link;
        this.type = type;
        this.priority = priority;
        this.activeTime = activeTime;
        this.expireTime = expireTime;
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

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Long getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(Long activeTime) {
        this.activeTime = activeTime;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    @Override
    public String toString() {
        return "BulletinInfo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", link='" + link + '\'' +
                ", type=" + type +
                ", priority=" + priority +
                ", activeTime=" + activeTime +
                ", expireTime=" + expireTime +
                '}';
    }

}

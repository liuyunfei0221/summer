package com.blue.portal.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * notice deploy
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "notice")
public class NoticeDeploy {

    private int expiresSecond;

    private Long id;

    private String title;

    private String content;

    private String link;

    private Integer type;

    public NoticeDeploy() {
    }

    public int getExpiresSecond() {
        return expiresSecond;
    }

    public void setExpiresSecond(int expiresSecond) {
        this.expiresSecond = expiresSecond;
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
        return "NoticeDeploy{" +
                "expiresSecond=" + expiresSecond +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", link='" + link + '\'' +
                ", type=" + type +
                '}';
    }

}

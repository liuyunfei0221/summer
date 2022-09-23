package com.blue.portal.api.model;

import com.blue.basic.serializer.IdentityDeserializer;
import com.blue.basic.serializer.IdentitySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;


/**
 * notice api info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class NoticeInfo implements Serializable {

    private static final long serialVersionUID = -100075804099435077L;

    @JsonSerialize(using = IdentitySerializer.class)
    @JsonDeserialize(using = IdentityDeserializer.class)
    private Long id;

    private String title;

    private String content;

    private String link;

    /**
     * @see com.blue.basic.constant.portal.NoticeType
     */
    private Integer type;

    public NoticeInfo() {
    }

    public NoticeInfo(Long id, String title, String content, String link, Integer type) {
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
        return "NoticeInfo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", link='" + link + '\'' +
                ", type=" + type +
                '}';
    }

}

package com.blue.media.api.model;


import com.blue.basic.serializer.Long2StringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * message template info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class MessageTemplateInfo implements Serializable {

    private static final long serialVersionUID = -5148017852176419254L;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long id;

    private String name;

    private String description;

    /**
     * @see com.blue.basic.constant.media.MessageType
     */
    private Integer type;

    /**
     * @see com.blue.basic.constant.media.MessageBusinessType
     */
    private Integer businessType;

    private String title;

    private Integer titlePlaceholderCount;

    private String content;

    private Integer contentPlaceholderCount;

    public MessageTemplateInfo() {
    }

    public MessageTemplateInfo(Long id, String name, String description, Integer type, Integer businessType, String title, Integer titlePlaceholderCount, String content, Integer contentPlaceholderCount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.businessType = businessType;
        this.title = title;
        this.titlePlaceholderCount = titlePlaceholderCount;
        this.content = content;
        this.contentPlaceholderCount = contentPlaceholderCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTitlePlaceholderCount() {
        return titlePlaceholderCount;
    }

    public void setTitlePlaceholderCount(Integer titlePlaceholderCount) {
        this.titlePlaceholderCount = titlePlaceholderCount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getContentPlaceholderCount() {
        return contentPlaceholderCount;
    }

    public void setContentPlaceholderCount(Integer contentPlaceholderCount) {
        this.contentPlaceholderCount = contentPlaceholderCount;
    }

    @Override
    public String toString() {
        return "MessageTemplateInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", businessType=" + businessType +
                ", title='" + title + '\'' +
                ", titlePlaceholderCount=" + titlePlaceholderCount +
                ", content='" + content + '\'' +
                ", contentPlaceholderCount=" + contentPlaceholderCount +
                '}';
    }

}
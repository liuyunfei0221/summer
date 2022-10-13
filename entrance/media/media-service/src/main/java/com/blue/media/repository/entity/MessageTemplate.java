package com.blue.media.repository.entity;


import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * message template
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class MessageTemplate implements Serializable {

    private static final long serialVersionUID = 2511921064553196993L;

    @Id
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

    private Long createTime;

    private Long updateTime;

    private Long creator;

    private Long updater;

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

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public Long getUpdater() {
        return updater;
    }

    public void setUpdater(Long updater) {
        this.updater = updater;
    }

    @Override
    public String toString() {
        return "MessageTemplate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", businessType=" + businessType +
                ", title='" + title + '\'' +
                ", titlePlaceholderCount=" + titlePlaceholderCount +
                ", content='" + content + '\'' +
                ", contentPlaceholderCount=" + contentPlaceholderCount +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", creator=" + creator +
                ", updater=" + updater +
                '}';
    }
    
}
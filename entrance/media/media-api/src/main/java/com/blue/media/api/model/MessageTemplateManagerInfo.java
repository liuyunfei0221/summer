package com.blue.media.api.model;


import java.io.Serializable;

/**
 * message template manager info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class MessageTemplateManagerInfo implements Serializable {

    private static final long serialVersionUID = 595870764153120707L;

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

    private String creatorName;

    private Long updater;

    private String updaterName;


    public MessageTemplateManagerInfo() {
    }

    public MessageTemplateManagerInfo(Long id, String name, String description, Integer type, Integer businessType, String title, Integer titlePlaceholderCount, String content, Integer contentPlaceholderCount,
                                      Long createTime, Long updateTime, Long creator, String creatorName, Long updater, String updaterName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.businessType = businessType;
        this.title = title;
        this.titlePlaceholderCount = titlePlaceholderCount;
        this.content = content;
        this.contentPlaceholderCount = contentPlaceholderCount;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.creator = creator;
        this.creatorName = creatorName;
        this.updater = updater;
        this.updaterName = updaterName;
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

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Long getUpdater() {
        return updater;
    }

    public void setUpdater(Long updater) {
        this.updater = updater;
    }

    public String getUpdaterName() {
        return updaterName;
    }

    public void setUpdaterName(String updaterName) {
        this.updaterName = updaterName;
    }

    @Override
    public String toString() {
        return "MessageTemplateManagerInfo{" +
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
                ", creatorName='" + creatorName + '\'' +
                ", updater=" + updater +
                ", updaterName='" + updaterName + '\'' +
                '}';
    }

}
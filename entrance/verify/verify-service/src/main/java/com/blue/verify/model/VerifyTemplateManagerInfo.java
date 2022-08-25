package com.blue.verify.model;


import com.blue.basic.serializer.IdentityDeserializer;
import com.blue.basic.serializer.IdentitySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * verify template manager info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class VerifyTemplateManagerInfo implements Serializable {

    private static final long serialVersionUID = -5178106130232785025L;

    @JsonSerialize(using = IdentitySerializer.class)
    @JsonDeserialize(using = IdentityDeserializer.class)
    private Long id;

    private String name;

    private String description;

    /**
     * @see com.blue.basic.constant.verify.VerifyType
     */
    private String type;

    /**
     * @see com.blue.basic.constant.verify.VerifyBusinessType
     */
    private String businessType;

    private String title;

    private String content;

    @JsonSerialize(using = IdentitySerializer.class)
    @JsonDeserialize(using = IdentityDeserializer.class)
    private Long createTime;

    @JsonSerialize(using = IdentitySerializer.class)
    @JsonDeserialize(using = IdentityDeserializer.class)
    private Long updateTime;

    @JsonSerialize(using = IdentitySerializer.class)
    @JsonDeserialize(using = IdentityDeserializer.class)
    private Long creator;

    private String creatorName;

    @JsonSerialize(using = IdentitySerializer.class)
    @JsonDeserialize(using = IdentityDeserializer.class)
    private Long updater;

    private String updaterName;

    public VerifyTemplateManagerInfo() {
    }

    public VerifyTemplateManagerInfo(Long id, String name, String description, String type, String businessType, String title, String content,
                                     Long createTime, Long updateTime, Long creator, String creatorName, Long updater, String updaterName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.businessType = businessType;
        this.title = title;
        this.content = content;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
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
        return "VerifyTemplateManagerInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", businessType=" + businessType +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", creator=" + creator +
                ", creatorName='" + creatorName + '\'' +
                ", updater=" + updater +
                ", updaterName='" + updaterName + '\'' +
                '}';
    }

}
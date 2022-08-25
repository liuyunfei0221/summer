package com.blue.verify.api.model;


import com.blue.basic.serializer.IdentityDeserializer;
import com.blue.basic.serializer.IdentitySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * verify template info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class VerifyTemplateInfo implements Serializable {

    private static final long serialVersionUID = 2627659518943780334L;

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

    public VerifyTemplateInfo() {
    }

    public VerifyTemplateInfo(Long id, String name, String description, String type, String businessType, String title, String content) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.businessType = businessType;
        this.title = title;
        this.content = content;
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

    @Override
    public String toString() {
        return "VerifyTemplateInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", businessType=" + businessType +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

}
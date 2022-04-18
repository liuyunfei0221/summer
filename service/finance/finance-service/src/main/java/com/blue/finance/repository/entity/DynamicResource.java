package com.blue.finance.repository.entity;

import java.io.Serializable;

import static com.blue.base.common.base.BlueChecker.isNull;

/**
 * dynamic resource entity
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class DynamicResource implements Serializable {

    private static final long serialVersionUID = -6495520302462631712L;

    private Long id;

    private Long organizationId;

    private Long handlerId;

    private String requestMethod;

    private Long uriPlaceholder;

    private String contentType;

    private String name;

    private String description;

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

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(Long handlerId) {
        this.handlerId = handlerId;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = isNull(requestMethod) ? null : requestMethod.trim();
    }

    public Long getUriPlaceholder() {
        return uriPlaceholder;
    }

    public void setUriPlaceholder(Long uriPlaceholder) {
        this.uriPlaceholder = uriPlaceholder;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = isNull(contentType) ? null : contentType.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = isNull(name) ? null : name.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = isNull(description) ? null : description.trim();
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
        return "DynamicResource{" +
                "id=" + id +
                ", organizationId=" + organizationId +
                ", handlerId=" + handlerId +
                ", requestMethod='" + requestMethod + '\'' +
                ", uriPlaceholder='" + uriPlaceholder + '\'' +
                ", contentType='" + contentType + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", creator=" + creator +
                ", updater=" + updater +
                '}';
    }

}
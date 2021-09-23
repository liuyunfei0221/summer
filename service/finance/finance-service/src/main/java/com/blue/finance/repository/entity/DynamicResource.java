package com.blue.finance.repository.entity;

/**
 * 动态资源
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public class DynamicResource {

    /**
     *
     */
    private Long id;

    /**
     *
     */
    private Long organizationId;

    /**
     *
     */
    private Long handlerId;

    /**
     *
     */
    private String requestMethod;

    /**
     *
     */
    private Long uriPlaceholder;

    /**
     *
     */
    private String contentType;

    /**
     *
     */
    private String name;

    /**
     *
     */
    private String description;

    /**
     *
     */
    private Long createTime;

    /**
     *
     */
    private Long updateTime;

    /**
     *
     */
    private Long creator;

    /**
     *
     */
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
        this.requestMethod = requestMethod == null ? null : requestMethod.trim();
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
        this.contentType = contentType == null ? null : contentType.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
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
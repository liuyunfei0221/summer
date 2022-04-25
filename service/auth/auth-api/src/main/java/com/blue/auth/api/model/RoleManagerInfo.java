package com.blue.auth.api.model;

import java.io.Serializable;

/**
 * role manager info for rest
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class RoleManagerInfo implements Serializable {

    private static final long serialVersionUID = 2423127593942076797L;

    /**
     * id
     */
    private Long id;

    /**
     * role name
     */
    private String name;

    /**
     * role disc
     */
    private String description;

    /**
     * role's level
     */
    private Integer level;

    /**
     * default role?
     */
    private Boolean isDefault;

    private Long createTime;

    private Long updateTime;

    private Long creator;

    private String creatorName;

    private Long updater;

    private String updaterName;

    public RoleManagerInfo() {
    }

    public RoleManagerInfo(Long id, String name, String description, Integer level, Boolean isDefault,
                           Long createTime, Long updateTime, Long creator, String creatorName, Long updater, String updaterName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.level = level;
        this.isDefault = isDefault;
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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
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
        return "RoleManagerInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", level=" + level +
                ", isDefault=" + isDefault +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", creator=" + creator +
                ", creatorName='" + creatorName + '\'' +
                ", updater=" + updater +
                ", updaterName='" + updaterName + '\'' +
                '}';
    }

}

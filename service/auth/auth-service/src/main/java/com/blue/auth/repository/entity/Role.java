package com.blue.auth.repository.entity;

import java.io.Serializable;

/**
 * role entity
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class Role implements Serializable {

    private static final long serialVersionUID = 1559136579322763468L;

    private Long id;

    private String name;

    private String description;

    private Integer level;

    /**
     * is the default role?
     */
    private Boolean isDefault;

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
        this.name = name == null ? null : name.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
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
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", level=" + level +
                ", isDefault=" + isDefault +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", creator=" + creator +
                ", updater=" + updater +
                '}';
    }

}
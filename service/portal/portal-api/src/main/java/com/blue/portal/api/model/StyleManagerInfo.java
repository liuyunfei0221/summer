package com.blue.portal.api.model;

import java.io.Serializable;


/**
 * style manager info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class StyleManagerInfo implements Serializable {

    private static final long serialVersionUID = 2353772661918508963L;

    private Long id;

    private String name;

    private String attributes;

    private Integer type;

    /**
     * @see com.blue.base.constant.base.Status
     */
    private Integer status;

    private Long createTime;

    private Long updateTime;

    private Long creator;

    private String creatorName;

    private Long updater;

    private String updaterName;

    public StyleManagerInfo() {
    }

    public StyleManagerInfo(Long id, String name, String attributes, Integer type, Integer status,
                            Long createTime, Long updateTime, Long creator, String creatorName, Long updater, String updaterName) {
        this.id = id;
        this.name = name;
        this.attributes = attributes;
        this.type = type;
        this.status = status;
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

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
        return "StyleManagerInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", attributes='" + attributes + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", creator=" + creator +
                ", creatorName='" + creatorName + '\'' +
                ", updater=" + updater +
                ", updaterName='" + updaterName + '\'' +
                '}';
    }

}

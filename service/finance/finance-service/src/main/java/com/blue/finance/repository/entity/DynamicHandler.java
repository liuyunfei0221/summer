package com.blue.finance.repository.entity;

import java.io.Serializable;

import static com.blue.base.common.base.BlueChecker.isNull;

/**
 * dynamic handler config entity
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class DynamicHandler implements Serializable {

    private static final long serialVersionUID = 3165687168951987180L;

    private Long id;

    private String name;

    private String description;

    private String handlerBean;

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
        this.name = isNull(name) ? null : name.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = isNull(description) ? null : description.trim();
    }

    public String getHandlerBean() {
        return handlerBean;
    }

    public void setHandlerBean(String handlerBean) {
        this.handlerBean = isNull(handlerBean) ? null : handlerBean.trim();
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
        return "DynamicHandler{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", handlerBean='" + handlerBean + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", creator=" + creator +
                ", updater=" + updater +
                '}';
    }

}
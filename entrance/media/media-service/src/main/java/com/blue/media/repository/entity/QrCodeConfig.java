package com.blue.media.repository.entity;


import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.List;

/**
 * qr code config
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class QrCodeConfig implements Serializable {

    private static final long serialVersionUID = 7723764623524543097L;

    @Id
    private Long id;

    private String name;

    private String description;

    /**
     * unique qr code type
     */
    private Integer type;

    /**
     * @see com.blue.base.constant.media.QrCodeGenType
     */
    private Integer genHandlerType;

    private String domain;

    private String pathToBeFilled;

    private Integer placeholderCount;

    /**
     * allowed role ids
     */
    private List<Long> allowedRoles;

    private Integer status;

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

    public Integer getGenHandlerType() {
        return genHandlerType;
    }

    public void setGenHandlerType(Integer genHandlerType) {
        this.genHandlerType = genHandlerType;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPathToBeFilled() {
        return pathToBeFilled;
    }

    public void setPathToBeFilled(String pathToBeFilled) {
        this.pathToBeFilled = pathToBeFilled;
    }

    public Integer getPlaceholderCount() {
        return placeholderCount;
    }

    public void setPlaceholderCount(Integer placeholderCount) {
        this.placeholderCount = placeholderCount;
    }

    public List<Long> getAllowedRoles() {
        return allowedRoles;
    }

    public void setAllowedRoles(List<Long> allowedRoles) {
        this.allowedRoles = allowedRoles;
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

    public Long getUpdater() {
        return updater;
    }

    public void setUpdater(Long updater) {
        this.updater = updater;
    }

    @Override
    public String toString() {
        return "QrCodeConfig{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", genHandlerType=" + genHandlerType +
                ", domain='" + domain + '\'' +
                ", pathToBeFilled='" + pathToBeFilled + '\'' +
                ", placeholderCount=" + placeholderCount +
                ", allowedRoles=" + allowedRoles +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", creator=" + creator +
                ", updater=" + updater +
                '}';
    }
    
}
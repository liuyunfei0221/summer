package com.blue.media.model;

import com.blue.auth.api.model.RoleInfo;
import com.blue.basic.constant.media.QrCodeType;

import java.io.Serializable;
import java.util.List;

/**
 * qr code config info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class QrCodeConfigManagerInfo implements Serializable {

    private static final long serialVersionUID = 4786656684754973382L;

    private Long id;

    private String name;

    private String description;

    /**
     * @see QrCodeType
     */
    private Integer type;

    private String domain;

    private String pathToBeFilled;

    private Integer placeholderCount;

    /**
     * allowed role ids
     */
    private List<RoleInfo> allowedRoles;

    private Long createTime;

    private Long updateTime;

    private Long creator;

    private String creatorName;

    private Long updater;

    private String updaterName;

    public QrCodeConfigManagerInfo() {
    }

    public QrCodeConfigManagerInfo(Long id, String name, String description, Integer type, String domain, String pathToBeFilled,
                                   Integer placeholderCount, List<RoleInfo> allowedRoles, Long createTime, Long updateTime,
                                   Long creator, String creatorName, Long updater, String updaterName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.domain = domain;
        this.pathToBeFilled = pathToBeFilled;
        this.placeholderCount = placeholderCount;
        this.allowedRoles = allowedRoles;
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

    public List<RoleInfo> getAllowedRoles() {
        return allowedRoles;
    }

    public void setAllowedRoles(List<RoleInfo> allowedRoles) {
        this.allowedRoles = allowedRoles;
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
        return "QrCodeConfigManagerInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", domain='" + domain + '\'' +
                ", pathToBeFilled='" + pathToBeFilled + '\'' +
                ", placeholderCount=" + placeholderCount +
                ", allowedRoles=" + allowedRoles +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", creator=" + creator +
                ", creatorName='" + creatorName + '\'' +
                ", updater=" + updater +
                ", updaterName='" + updaterName + '\'' +
                '}';
    }

}
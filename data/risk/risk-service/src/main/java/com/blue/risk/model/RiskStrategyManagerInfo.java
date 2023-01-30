package com.blue.risk.model;

import com.blue.basic.serializer.Long2StringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.Map;


/**
 * risk strategy manager info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class RiskStrategyManagerInfo implements Serializable {

    private static final long serialVersionUID = -6322346704931894292L;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long id;

    private String name;

    private String description;

    /**
     * hit type
     *
     * @see com.blue.basic.constant.risk.RiskType
     */
    private Integer type;

    /**
     * risk attributes
     */
    private Map<String, String> attributes;

    private Boolean enable;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long createTime;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long updateTime;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long creator;

    private String creatorName;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long updater;

    private String updaterName;

    public RiskStrategyManagerInfo() {
    }

    public RiskStrategyManagerInfo(Long id, String name, String description, Integer type, Map<String, String> attributes, Boolean enable,
                                   Long createTime, Long updateTime, Long creator, String creatorName, Long updater, String updaterName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.attributes = attributes;
        this.enable = enable;
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

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
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
        return "RiskStrategyManagerInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", attributes=" + attributes +
                ", enable=" + enable +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", creator=" + creator +
                ", creatorName='" + creatorName + '\'' +
                ", updater=" + updater +
                ", updaterName='" + updaterName + '\'' +
                '}';
    }

}
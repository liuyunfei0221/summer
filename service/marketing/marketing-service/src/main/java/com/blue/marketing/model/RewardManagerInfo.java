package com.blue.marketing.model;


import com.blue.basic.serializer.Long2StringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * reward manager info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class RewardManagerInfo implements Serializable {

    private static final long serialVersionUID = 8175699924429543753L;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long id;

    private String name;

    private String detail;

    private String link;

    /**
     * reward type
     *
     * @see com.blue.basic.constant.marketing.RewardType
     */
    private Integer type;

    /**
     * reward json
     */
    private String data;

    /**
     * @see com.blue.basic.constant.common.Status
     */
    private Integer status;

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

    public RewardManagerInfo() {
    }

    public RewardManagerInfo(Long id, String name, String detail, String link, Integer type, String data, Integer status, Long createTime, Long updateTime, Long creator, String creatorName, Long updater, String updaterName) {
        this.id = id;
        this.name = name;
        this.detail = detail;
        this.link = link;
        this.type = type;
        this.data = data;
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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
        return "RewardManagerInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", detail='" + detail + '\'' +
                ", link='" + link + '\'' +
                ", type=" + type +
                ", data='" + data + '\'' +
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

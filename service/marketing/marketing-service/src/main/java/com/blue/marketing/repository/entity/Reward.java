package com.blue.marketing.repository.entity;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isNull;

/**
 * reward entity
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class Reward implements Serializable {

    private static final long serialVersionUID = 5585565777762900601L;

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
     * reward status
     *
     * @see com.blue.basic.constant.common.Status
     */
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
        this.name = isNull(name) ? null : name.trim();
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = isNull(detail) ? null : detail.trim();
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = isNull(link) ? null : link.trim();
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
        this.data = isNull(data) ? null : data.trim();
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
        return "Reward{" +
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
                ", updater=" + updater +
                '}';
    }

}
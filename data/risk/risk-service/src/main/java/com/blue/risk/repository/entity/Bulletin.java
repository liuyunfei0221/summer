package com.blue.risk.repository.entity;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isNull;

/**
 * test entity
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public class Bulletin implements Serializable {

    private static final long serialVersionUID = -9153106184329621167L;

    private Long id;

    private String title;

    private String content;

    private String link;

    /**
     * @see com.blue.basic.constant.portal.BulletinType
     */
    private Integer type;

    /**
     * @see com.blue.basic.constant.common.Status
     */
    private Integer status;

    private Integer order;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = isNull(title) ? null : title.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = isNull(content) ? null : content.trim();
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
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

}
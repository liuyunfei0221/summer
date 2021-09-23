package com.blue.shine.api.model;

import java.io.Serializable;

/**
 * 公益信息
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class ShineInfo implements Serializable {

    private static final long serialVersionUID = 4417623130276820000L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 优先级
     */
    private Integer order;

    /**
     * 创建时间
     */
    private Long createTime;

    public ShineInfo() {
    }

    public ShineInfo(Long id, String title, String content, Integer order, Long createTime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.order = order;
        this.createTime = createTime;
    }

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
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    @Override
    public String toString() {
        return "ShineInfo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", order=" + order +
                ", createTime=" + createTime +
                '}';
    }

}

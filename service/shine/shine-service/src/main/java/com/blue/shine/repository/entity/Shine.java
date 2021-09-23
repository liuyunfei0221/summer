package com.blue.shine.repository.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.io.Serializable;

/**
 * @author liuyunfei
 * @date 2021/9/16
 * @apiNote
 */
@SuppressWarnings("unused")
public class Shine implements Serializable {

    /**
     * 主键
     */
    @Id
    private Long id;

    /**
     * 标题
     */
    @Indexed(unique = true)
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

    /**
     * 修改时间
     */
    private Long updateTime;

    /**
     * 创建人
     */
    private Long creator;

    /**
     * 修改人
     */
    private Long updater;

    public Shine() {
    }

    public Shine(Long id, String title, String content, Integer order, Long createTime, Long updateTime, Long creator, Long updater) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.order = order;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.creator = creator;
        this.updater = updater;
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
        return "Shine{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", order=" + order +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", creator=" + creator +
                ", updater=" + updater +
                '}';
    }

}

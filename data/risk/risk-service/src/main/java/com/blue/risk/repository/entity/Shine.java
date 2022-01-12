package com.blue.risk.repository.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.io.Serializable;

/**
 * shine info
 *
 * @author liuyunfei
 * @date 2021/9/16
 * @apiNote
 */
@SuppressWarnings("unused")
public class Shine implements Serializable {

    private static final long serialVersionUID = -5386491585245489265L;

    @Id
    private Long id;

    @Indexed(unique = true)
    private String title;

    private String content;

    private Integer order;

    private Long createTime;

    private Long updateTime;

    private Long creator;

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

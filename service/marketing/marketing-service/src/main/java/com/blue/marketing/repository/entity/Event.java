package com.blue.marketing.repository.entity;

/**
 * 营销事件表
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class Event {

    /**
     * 主键
     */
    private Long id;

    /**
     * 事件类型
     */
    private Integer type;

    /**
     * json格式事件数据
     */
    private String data;

    /**
     * 事件状态 0未处理 1已处理
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 创建人
     */
    private Long creator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        this.data = data == null ? null : data.trim();
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

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

}
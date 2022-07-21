package com.blue.auth.repository.entity;

import java.io.Serializable;

/**
 * role resource relation entity
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class RoleResRelation implements Serializable {

    private static final long serialVersionUID = -806383752202349629L;

    private Long id;

    private Long roleId;

    /**
     * resource id
     */
    private Long resId;

    private Long createTime;

    private Long creator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getResId() {
        return resId;
    }

    public void setResId(Long resId) {
        this.resId = resId;
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

    @Override
    public String toString() {
        return "RoleResRelation{" +
                "id=" + id +
                ", roleId=" + roleId +
                ", resId=" + resId +
                ", createTime=" + createTime +
                ", creator=" + creator +
                '}';
    }
}
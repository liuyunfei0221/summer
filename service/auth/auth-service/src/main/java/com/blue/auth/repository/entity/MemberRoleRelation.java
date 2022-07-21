package com.blue.auth.repository.entity;

import java.io.Serializable;

/**
 * member role relation entity
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class MemberRoleRelation implements Serializable {

    private static final long serialVersionUID = -2584702552557832911L;

    private Long id;

    private Long memberId;

    private Long roleId;

    private Long createTime;

    private Long creator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
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
        return "MemberRoleRelation{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", roleId=" + roleId +
                ", createTime=" + createTime +
                ", creator=" + creator +
                '}';
    }

}
package com.blue.secure.model;

import java.io.Serializable;

/**
 * member-role-relation param
 *
 * @author liuyunfei
 * @date 2021/11/9
 * @apiNote
 */
public final class MemberRoleRelationParam implements Serializable {

    private static final long serialVersionUID = -2777562063739105469L;

    private Long memberId;

    private Long roleId;

    public MemberRoleRelationParam(Long memberId, Long roleId) {
        this.memberId = memberId;
        this.roleId = roleId;
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

    @Override
    public String toString() {
        return "MemberRoleRelationParam{" +
                "memberId=" + memberId +
                ", roleId=" + roleId +
                '}';
    }

}

package com.blue.auth.api.model;

import java.io.Serializable;

/**
 * @author liuyunfei
 * @date 2021/10/19
 * @apiNote
 */
@SuppressWarnings("unused")
public final class MemberRoleRelationInfo implements Serializable {

    private static final long serialVersionUID = -8445536816680117649L;

    private Long memberId;

    private RoleInfo roleInfo;

    public MemberRoleRelationInfo() {
    }

    public MemberRoleRelationInfo(Long memberId, RoleInfo roleInfo) {
        this.memberId = memberId;
        this.roleInfo = roleInfo;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public RoleInfo getRoleInfo() {
        return roleInfo;
    }

    public void setRoleInfo(RoleInfo roleInfo) {
        this.roleInfo = roleInfo;
    }

    @Override
    public String toString() {
        return "MemberRoleRelationInfo{" +
                "memberId=" + memberId +
                ", roleInfo=" + roleInfo +
                '}';
    }
}

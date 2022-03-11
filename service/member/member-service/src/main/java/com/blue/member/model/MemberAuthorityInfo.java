package com.blue.member.model;

import com.blue.member.api.model.MemberInfo;
import com.blue.auth.api.model.RoleInfo;

import java.io.Serializable;

/**
 * member authority info
 *
 * @author liuyunfei
 * @date 2021/10/19
 * @apiNote
 */
@SuppressWarnings("unused")
public final class MemberAuthorityInfo implements Serializable {

    private static final long serialVersionUID = 225423587277804228L;

    private MemberInfo memberInfo;

    private RoleInfo roleInfo;

    public MemberAuthorityInfo() {
    }

    public MemberAuthorityInfo(MemberInfo memberInfo, RoleInfo roleInfo) {
        this.memberInfo = memberInfo;
        this.roleInfo = roleInfo;
    }

    public MemberInfo getMemberInfo() {
        return memberInfo;
    }

    public void setMemberInfo(MemberInfo memberInfo) {
        this.memberInfo = memberInfo;
    }

    public RoleInfo getRoleInfo() {
        return roleInfo;
    }

    public void setRoleInfo(RoleInfo roleInfo) {
        this.roleInfo = roleInfo;
    }

    @Override
    public String toString() {
        return "MemberAuthorityInfo{" +
                "memberInfo=" + memberInfo +
                ", roleInfo=" + roleInfo +
                '}';
    }

}

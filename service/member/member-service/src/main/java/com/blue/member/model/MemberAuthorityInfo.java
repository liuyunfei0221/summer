package com.blue.member.model;

import com.blue.auth.api.model.RoleInfo;
import com.blue.member.api.model.MemberInfo;

import java.io.Serializable;
import java.util.List;

/**
 * member authority info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class MemberAuthorityInfo implements Serializable {

    private static final long serialVersionUID = 225423587277804228L;

    private MemberInfo memberInfo;

    private List<RoleInfo> roleInfos;

    public MemberAuthorityInfo() {
    }

    public MemberAuthorityInfo(MemberInfo memberInfo, List<RoleInfo> roleInfos) {
        this.memberInfo = memberInfo;
        this.roleInfos = roleInfos;
    }

    public MemberInfo getMemberInfo() {
        return memberInfo;
    }

    public void setMemberInfo(MemberInfo memberInfo) {
        this.memberInfo = memberInfo;
    }

    public List<RoleInfo> getRoleInfos() {
        return roleInfos;
    }

    public void setRoleInfos(List<RoleInfo> roleInfos) {
        this.roleInfos = roleInfos;
    }

    @Override
    public String toString() {
        return "MemberAuthorityInfo{" +
                "memberInfo=" + memberInfo +
                ", roleInfos=" + roleInfos +
                '}';
    }

}

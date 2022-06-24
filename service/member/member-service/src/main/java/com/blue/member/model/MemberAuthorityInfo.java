package com.blue.member.model;

import com.blue.auth.api.model.RoleInfo;
import com.blue.member.api.model.MemberBasicInfo;

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

    private MemberBasicInfo memberBasicInfo;

    private List<RoleInfo> roleInfos;

    public MemberAuthorityInfo() {
    }

    public MemberAuthorityInfo(MemberBasicInfo memberBasicInfo, List<RoleInfo> roleInfos) {
        this.memberBasicInfo = memberBasicInfo;
        this.roleInfos = roleInfos;
    }

    public MemberBasicInfo getMemberBasicInfo() {
        return memberBasicInfo;
    }

    public void setMemberBasicInfo(MemberBasicInfo memberBasicInfo) {
        this.memberBasicInfo = memberBasicInfo;
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
                "memberBasicInfo=" + memberBasicInfo +
                ", roleInfos=" + roleInfos +
                '}';
    }

}

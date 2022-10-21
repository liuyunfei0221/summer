package com.blue.auth.api.model;

import com.blue.basic.serializer.Long2StringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.List;

/**
 * member and role info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class MemberRoleInfo implements Serializable {

    private static final long serialVersionUID = -8445536816680117649L;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long memberId;

    private List<RoleInfo> roleInfos;

    public MemberRoleInfo() {
    }

    public MemberRoleInfo(Long memberId, List<RoleInfo> roleInfos) {
        this.memberId = memberId;
        this.roleInfos = roleInfos;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public List<RoleInfo> getRoleInfos() {
        return roleInfos;
    }

    public void setRoleInfos(List<RoleInfo> roleInfos) {
        this.roleInfos = roleInfos;
    }

    @Override
    public String toString() {
        return "MemberRoleRelationInfo{" +
                "memberId=" + memberId +
                ", roleInfos=" + roleInfos +
                '}';
    }
}

package com.blue.secure.model;

import java.io.Serializable;
import java.util.List;

/**
 * role-resource-relation param
 *
 * @author liuyunfei
 * @date 2021/11/9
 * @apiNote
 */
public final class RoleResRelationParam implements Serializable {

    private static final long serialVersionUID = -4707222541690629552L;

    private Long roleId;

    private List<Long> resIds;

    public RoleResRelationParam(Long roleId, List<Long> resIds) {
        this.roleId = roleId;
        this.resIds = resIds;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public List<Long> getResIds() {
        return resIds;
    }

    public void setResIds(List<Long> resIds) {
        this.resIds = resIds;
    }

    @Override
    public String toString() {
        return "RoleResRelationParam{" +
                "roleId=" + roleId +
                ", resIds=" + resIds +
                '}';
    }

}

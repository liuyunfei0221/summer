package com.blue.auth.api.model;

import com.blue.base.inter.Asserter;
import com.blue.base.model.exps.BlueException;

import java.io.Serializable;
import java.util.List;

import static com.blue.base.common.base.BlueChecker.isEmpty;
import static com.blue.base.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.base.constant.common.ResponseElement.INVALID_IDENTITY;

/**
 * role-resource-relation param
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class RoleResRelationParam implements Serializable, Asserter {

    private static final long serialVersionUID = -4707222541690629552L;

    private Long roleId;

    private List<Long> resIds;

    public RoleResRelationParam(Long roleId, List<Long> resIds) {
        this.roleId = roleId;
        this.resIds = resIds;
    }

    @Override
    public void asserts() {
        if (isInvalidIdentity(roleId) || isEmpty(resIds))
            throw new BlueException(INVALID_IDENTITY);
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

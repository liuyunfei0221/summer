package com.blue.auth.api.model;

import com.blue.base.inter.Asserter;
import com.blue.base.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.base.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.base.constant.base.ResponseElement.INVALID_IDENTITY;

/**
 * member-role-relation param
 *
 * @author liuyunfei
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
public final class MemberRoleRelationParam implements Serializable, Asserter {

    private static final long serialVersionUID = -2777562063739105469L;

    private Long memberId;

    private Long roleId;

    public MemberRoleRelationParam(Long memberId, Long roleId) {
        this.memberId = memberId;
        this.roleId = roleId;
    }

    @Override
    public void asserts() {
        if (isInvalidIdentity(memberId) || isInvalidIdentity(roleId))
            throw new BlueException(INVALID_IDENTITY);
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

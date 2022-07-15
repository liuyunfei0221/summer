package com.blue.auth.api.model;

import com.blue.basic.inter.Asserter;
import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;
import java.util.List;

import static com.blue.basic.common.base.BlueChecker.isInvalidIdentities;
import static com.blue.basic.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;

/**
 * member-role-relation update param
 *
 * @author liuyunfei
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
public final class MemberRoleRelationUpdateParam implements Serializable, Asserter {

    private static final long serialVersionUID = -2777562063739105469L;

    private Long memberId;

    private List<Long> roleIds;

    public MemberRoleRelationUpdateParam(Long memberId, List<Long> roleIds) {
        this.memberId = memberId;
        this.roleIds = roleIds;
    }

    @Override
    public void asserts() {
        if (isInvalidIdentity(memberId) || isInvalidIdentities(roleIds))
            throw new BlueException(INVALID_IDENTITY);
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }

    @Override
    public String toString() {
        return "MemberRoleRelationParam{" +
                "memberId=" + memberId +
                ", roleIds=" + roleIds +
                '}';
    }

}

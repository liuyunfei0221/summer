package com.blue.auth.api.model;

import com.blue.basic.inter.Asserter;
import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;

/**
 * member-role-relation insert/delete param
 *
 * @author liuyunfei
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
public final class MemberRoleRelationInsertOrDeleteParam implements Serializable, Asserter {

    private static final long serialVersionUID = -7855696675627510685L;

    private Long memberId;

    private Long roleId;

    public MemberRoleRelationInsertOrDeleteParam(Long memberId, Long roleId) {
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
        return "MemberRoleRelationInsertOrDeleteParam{" +
                "memberId=" + memberId +
                ", roleId=" + roleId +
                '}';
    }

}

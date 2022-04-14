package com.blue.auth.api.model;

import com.blue.base.inter.Asserter;
import com.blue.base.model.exps.BlueException;

import java.io.Serializable;
import java.util.List;

import static com.blue.base.common.base.BlueChecker.isEmpty;
import static com.blue.base.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.base.constant.base.ResponseElement.INVALID_PARAM;

/**
 * member credential infoS with member id
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class MemberCredentialInfo implements Serializable, Asserter {

    private static final long serialVersionUID = 8079056879120261243L;

    private Long memberId;

    private List<CredentialInfo> credentials;

    public MemberCredentialInfo() {
    }

    public MemberCredentialInfo(Long memberId, List<CredentialInfo> credentials) {
        this.memberId = memberId;
        this.credentials = credentials;
    }

    @Override
    public void asserts() {
        if (isInvalidIdentity(this.memberId) || isEmpty(this.credentials))
            throw new BlueException(INVALID_PARAM);
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public List<CredentialInfo> getCredentials() {
        return credentials;
    }

    public void setCredentials(List<CredentialInfo> credentials) {
        this.credentials = credentials;
    }

    @Override
    public String toString() {
        return "MemberCredentialInfo{" +
                "memberId=" + memberId +
                ", credentials=" + credentials +
                '}';
    }

}

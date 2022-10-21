package com.blue.auth.api.model;

import com.blue.basic.inter.Asserter;
import com.blue.basic.model.exps.BlueException;
import com.blue.basic.serializer.Long2StringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.List;

import static com.blue.basic.common.base.BlueChecker.isEmpty;
import static com.blue.basic.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.basic.constant.common.ResponseElement.INVALID_PARAM;

/**
 * member credential infoS with member id
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class MemberCredentialInfo implements Serializable, Asserter {

    private static final long serialVersionUID = 8079056879120261243L;

    @JsonSerialize(using = Long2StringSerializer.class)
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

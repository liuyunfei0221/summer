package com.blue.finance.api.model;

import com.blue.basic.serializer.IdentityDeserializer;
import com.blue.basic.serializer.IdentitySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * member finance info with member id
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class MemberFinanceInfo implements Serializable {

    private static final long serialVersionUID = 1648718390071140982L;

    @JsonSerialize(using = IdentitySerializer.class)
    @JsonDeserialize(using = IdentityDeserializer.class)
    private Long memberId;

    public MemberFinanceInfo() {
    }

    public MemberFinanceInfo(Long memberId) {
        this.memberId = memberId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    @Override
    public String toString() {
        return "MemberCredentialInfo{" +
                "memberId=" + memberId +
                '}';
    }

}

package com.blue.finance.api.model;

import java.io.Serializable;

/**
 * member finance infos with member id
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class MemberFinanceInfo implements Serializable {

    private static final long serialVersionUID = 1648718390071140982L;

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

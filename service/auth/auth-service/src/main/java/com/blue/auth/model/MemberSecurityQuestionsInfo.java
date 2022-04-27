package com.blue.auth.model;

import java.io.Serializable;
import java.util.List;

/**
 * member and security question info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class MemberSecurityQuestionsInfo implements Serializable {

    private static final long serialVersionUID = -8705792063313129820L;

    private Long memberId;

    private String memberName;

    private List<SecurityQuestionInfo> securityQuestionInfos;

    public MemberSecurityQuestionsInfo() {
    }

    public MemberSecurityQuestionsInfo(Long memberId, String memberName, List<SecurityQuestionInfo> securityQuestionInfos) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.securityQuestionInfos = securityQuestionInfos;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public List<SecurityQuestionInfo> getSecurityQuestionInfos() {
        return securityQuestionInfos;
    }

    public void setSecurityQuestionInfos(List<SecurityQuestionInfo> securityQuestionInfos) {
        this.securityQuestionInfos = securityQuestionInfos;
    }

    @Override
    public String toString() {
        return "MemberSecurityQuestionsInfo{" +
                "memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", securityQuestionInfos=" + securityQuestionInfos +
                '}';
    }

}

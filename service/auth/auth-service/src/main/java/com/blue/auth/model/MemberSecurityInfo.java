package com.blue.auth.model;

import com.blue.member.api.model.MemberBasicInfo;

import java.io.Serializable;
import java.util.List;

/**
 * member and security info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class MemberSecurityInfo implements Serializable {

    private static final long serialVersionUID = -8705792063313129820L;

    private Long memberId;

    private MemberBasicInfo memberBasicInfo;

    private List<CredentialHistoryInfo> credentialHistoryInfos;

    private List<SecurityQuestionInfo> securityQuestionInfos;

    public MemberSecurityInfo() {
    }

    public MemberSecurityInfo(Long memberId, MemberBasicInfo memberBasicInfo, List<CredentialHistoryInfo> credentialHistoryInfos, List<SecurityQuestionInfo> securityQuestionInfos) {
        this.memberId = memberId;
        this.memberBasicInfo = memberBasicInfo;
        this.credentialHistoryInfos = credentialHistoryInfos;
        this.securityQuestionInfos = securityQuestionInfos;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public MemberBasicInfo getMemberBasicInfo() {
        return memberBasicInfo;
    }

    public void setMemberBasicInfo(MemberBasicInfo memberBasicInfo) {
        this.memberBasicInfo = memberBasicInfo;
    }

    public List<CredentialHistoryInfo> getCredentialHistoryInfos() {
        return credentialHistoryInfos;
    }

    public void setCredentialHistoryInfos(List<CredentialHistoryInfo> credentialHistoryInfos) {
        this.credentialHistoryInfos = credentialHistoryInfos;
    }

    public List<SecurityQuestionInfo> getSecurityQuestionInfos() {
        return securityQuestionInfos;
    }

    public void setSecurityQuestionInfos(List<SecurityQuestionInfo> securityQuestionInfos) {
        this.securityQuestionInfos = securityQuestionInfos;
    }

    @Override
    public String toString() {
        return "MemberSecurityInfo{" +
                "memberId=" + memberId +
                ", memberBasicInfo=" + memberBasicInfo +
                ", credentialHistoryInfos=" + credentialHistoryInfos +
                ", securityQuestionInfos=" + securityQuestionInfos +
                '}';
    }

}

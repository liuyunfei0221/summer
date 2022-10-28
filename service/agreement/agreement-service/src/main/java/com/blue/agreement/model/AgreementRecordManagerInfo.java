package com.blue.agreement.model;

import com.blue.agreement.api.model.AgreementInfo;

import java.io.Serializable;

/**
 * agreement record info for manager
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class AgreementRecordManagerInfo implements Serializable {

    private static final long serialVersionUID = -8772296164848565374L;

    private Long id;

    private Long memberId;

    private String memberName;

    private Long agreementId;

    private AgreementInfo agreementInfo;

    private Long createTime;

    public AgreementRecordManagerInfo() {
    }

    public AgreementRecordManagerInfo(Long id, Long memberId, String memberName, Long agreementId, AgreementInfo agreementInfo, Long createTime) {
        this.id = id;
        this.memberId = memberId;
        this.memberName = memberName;
        this.agreementId = agreementId;
        this.agreementInfo = agreementInfo;
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(Long agreementId) {
        this.agreementId = agreementId;
    }

    public AgreementInfo getAgreementInfo() {
        return agreementInfo;
    }

    public void setAgreementInfo(AgreementInfo agreementInfo) {
        this.agreementInfo = agreementInfo;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "AgreementRecordManagerInfo{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", agreementId=" + agreementId +
                ", agreementInfo=" + agreementInfo +
                ", createTime=" + createTime +
                '}';
    }

}

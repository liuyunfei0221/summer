package com.blue.agreement.repository.entity;

import org.springframework.data.annotation.Id;

import java.io.Serializable;

@SuppressWarnings("unused")
public class AgreementRecord implements Serializable {

    private static final long serialVersionUID = -4675634237640261834L;

    @Id
    private Long id;

    private Long memberId;

    private Long agreementId;

    private Long createTime;

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

    public Long getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(Long agreementId) {
        this.agreementId = agreementId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "AgreementRecord{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", agreementId=" + agreementId +
                ", createTime=" + createTime +
                '}';
    }

}
package com.blue.agreement.repository.entity;

import org.springframework.data.annotation.Id;

import java.io.Serializable;

@SuppressWarnings("unused")
public class MemberAgreementRecord implements Serializable {

    private static final long serialVersionUID = -4675634237640261834L;

    @Id
    private Long id;

    private Long memberId;

    private Long agreementId;

    private Integer agree;

    private Long createTime;

    private Long updateTime;

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

    public Integer getAgree() {
        return agree;
    }

    public void setAgree(Integer agree) {
        this.agree = agree;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "MemberAgreementRecord{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", agreementId=" + agreementId +
                ", agree=" + agree +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

}
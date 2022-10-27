package com.blue.agreement.api.model;

import com.blue.basic.serializer.Long2StringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * agreement record info for rest
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public class AgreementRecordInfo implements Serializable {

    private static final long serialVersionUID = 8782949181149183651L;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long id;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long memberId;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long agreementId;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long createTime;

    public AgreementRecordInfo() {
    }

    public AgreementRecordInfo(Long id, Long memberId, Long agreementId, Long createTime) {
        this.id = id;
        this.memberId = memberId;
        this.agreementId = agreementId;
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
        return "AgreementRecordInfo{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", agreementId=" + agreementId +
                ", createTime=" + createTime +
                '}';
    }

}

package com.blue.agreement.model;

import com.blue.agreement.constant.AgreementRecordSortAttribute;
import com.blue.basic.model.common.SortCondition;

import java.io.Serializable;

import static com.blue.basic.constant.common.SortType.DESC;

/**
 * agreement record condition for select
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class AgreementRecordCondition extends SortCondition implements Serializable {

    private static final long serialVersionUID = -8425561532136934451L;

    private Long id;

    private Long memberId;

    private Long agreementId;

    private Long createTimeBegin;

    private Long createTimeEnd;

    public AgreementRecordCondition() {
        super(AgreementRecordSortAttribute.CREATE_TIME.attribute, DESC.identity);
    }

    public AgreementRecordCondition(String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
    }

    public AgreementRecordCondition(Long id, Long memberId, Long agreementId, Long createTimeBegin, Long createTimeEnd, String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
        this.id = id;
        this.memberId = memberId;
        this.agreementId = agreementId;
        this.createTimeBegin = createTimeBegin;
        this.createTimeEnd = createTimeEnd;
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

    public Long getCreateTimeBegin() {
        return createTimeBegin;
    }

    public void setCreateTimeBegin(Long createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }

    public Long getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(Long createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    @Override
    public String toString() {
        return "AgreementRecordCondition{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", agreementId=" + agreementId +
                ", createTimeBegin=" + createTimeBegin +
                ", createTimeEnd=" + createTimeEnd +
                ", sortAttribute='" + sortAttribute + '\'' +
                ", sortType='" + sortType + '\'' +
                '}';
    }

}

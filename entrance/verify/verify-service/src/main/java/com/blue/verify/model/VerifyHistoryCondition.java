package com.blue.verify.model;

import com.blue.basic.constant.common.SortType;
import com.blue.basic.constant.verify.BusinessType;
import com.blue.basic.model.common.SortCondition;
import com.blue.verify.constant.VerifyHistorySortAttribute;

import java.io.Serializable;

/**
 * verify history condition for select
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class VerifyHistoryCondition extends SortCondition implements Serializable {

    private static final long serialVersionUID = -3507874675823426127L;

    private Long id;

    /**
     * @see com.blue.basic.constant.verify.VerifyType
     */
    private String verifyType;

    /**
     * @see BusinessType
     */
    private String businessType;

    private String destination;

    private String verify;

    private String requestIp;

    private Long createTimeBegin;

    private Long createTimeEnd;

    public VerifyHistoryCondition() {
        super(VerifyHistorySortAttribute.ID.attribute, SortType.DESC.identity);
    }

    public VerifyHistoryCondition(String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
    }

    public VerifyHistoryCondition(Long id, String verifyType, String businessType, String destination, String verify,
                                  String requestIp, Long createTimeBegin, Long createTimeEnd, String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
        this.id = id;
        this.verifyType = verifyType;
        this.businessType = businessType;
        this.destination = destination;
        this.verify = verify;
        this.requestIp = requestIp;
        this.createTimeBegin = createTimeBegin;
        this.createTimeEnd = createTimeEnd;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVerifyType() {
        return verifyType;
    }

    public void setVerifyType(String verifyType) {
        this.verifyType = verifyType;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    public String getRequestIp() {
        return requestIp;
    }

    public void setRequestIp(String requestIp) {
        this.requestIp = requestIp;
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
        return "VerifyHistoryCondition{" +
                "id=" + id +
                ", verifyType='" + verifyType + '\'' +
                ", businessType='" + businessType + '\'' +
                ", destination='" + destination + '\'' +
                ", verify='" + verify + '\'' +
                ", requestIp='" + requestIp + '\'' +
                ", createTimeBegin=" + createTimeBegin +
                ", createTimeEnd=" + createTimeEnd +
                ", sortAttribute='" + sortAttribute + '\'' +
                ", sortType='" + sortType + '\'' +
                '}';
    }

}

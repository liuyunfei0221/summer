package com.blue.agreement.constant;

/**
 * column names of agreement record
 *
 * @author liuyunfei
 */
public enum AgreementRecordColumnName {

    /**
     * ID
     */
    ID("id"),

    /**
     * member id
     */
    MEMBER_ID("memberId"),

    /**
     * agreement id
     */
    AGREEMENT_ID("agreementId"),

    /**
     * create time
     */
    CREATE_TIME("createTime");

    public final String name;

    AgreementRecordColumnName(String name) {
        this.name = name;
    }

}

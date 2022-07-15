package com.blue.basic.constant.member;

/**
 * real name validator type
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AlibabaEnumConstantsMustHaveComment"})
public enum RealNameValidatorType {

    /**
     * ali validator
     */
    ALI("ali"),

    /**
     * tencent validator
     */
    TENCENT("tencent");

    public final String identity;

    RealNameValidatorType(String identity) {
        this.identity = identity;
    }

}

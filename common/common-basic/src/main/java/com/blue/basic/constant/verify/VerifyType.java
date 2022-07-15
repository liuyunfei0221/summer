package com.blue.basic.constant.verify;

import com.blue.basic.constant.common.RandomType;

import static com.blue.basic.constant.common.RandomType.ALPHANUMERIC;
import static com.blue.basic.constant.common.RandomType.NUMERIC;

/**
 * verify type
 *
 * @author liuyunfei
 */
public enum VerifyType {

    /**
     * image verify
     */
    IMAGE("IMAGE", ALPHANUMERIC),

    /**
     * sms code verify
     */
    SMS("SMS", NUMERIC),

    /**
     * mail verify
     */
    MAIL("MAIL", NUMERIC);

    public final String identity;

    public final RandomType randomType;

    VerifyType(String identity, RandomType randomType) {
        this.identity = identity;
        this.randomType = randomType;
    }
}

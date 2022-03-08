package com.blue.base.constant.verify;

import com.blue.base.constant.base.RandomType;

import static com.blue.base.constant.base.RandomType.ALPHANUMERIC;
import static com.blue.base.constant.base.RandomType.NUMERIC;

/**
 * verify type
 *
 * @author liuyunfei
 * @date 2021/12/23
 * @apiNote
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
    MAIL("MAIL", ALPHANUMERIC);

    public final String identity;

    public final RandomType randomType;

    VerifyType(String identity, RandomType randomType) {
        this.identity = identity;
        this.randomType = randomType;
    }
}

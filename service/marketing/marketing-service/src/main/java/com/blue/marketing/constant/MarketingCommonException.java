package com.blue.marketing.constant;

import com.blue.base.model.exps.BlueException;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;

/**
 * @author liuyunfei
 * @date 2021/11/3
 * @apiNote
 */
@SuppressWarnings("unused")
public enum MarketingCommonException {

    /**
     * REPEAT_SIGN_IN_EXP
     */
    REPEAT_SIGN_IN_EXP(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "Please do not sign in again", null));

    public BlueException exp;

    MarketingCommonException(BlueException exp) {
        this.exp = exp;
    }

}

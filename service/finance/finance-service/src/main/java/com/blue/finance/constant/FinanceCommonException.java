package com.blue.finance.constant;

import com.blue.base.model.exps.BlueException;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseMessage.*;

/**
 * @author liuyunfei
 * @date 2021/11/3
 * @apiNote
 */
@SuppressWarnings("unused")
public enum FinanceCommonException {

    /**
     * MEMBER_NOT_HAS_A_ROLE_EXP
     */
    MEMBER_NOT_HAS_A_ROLE_EXP(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, MEMBER_NOT_HAS_A_ROLE.message)),

    /**
     * MEMBER_ALREADY_HAS_A_ROLE_EXP
     */
    MEMBER_ALREADY_HAS_A_ROLE_EXP(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, MEMBER_ALREADY_HAS_A_ROLE.message)),

    /**
     * BLANK_NAME_EXP
     */
    BLANK_NAME_EXP(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "name can't be blank")),

    /**
     * BLANK_DESC_EXP
     */
    BLANK_DESC_EXP(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "description can't be blank")),

    /**
     * ACCOUNT_HAS_BEEN_FROZEN_EXP
     */
    ACCOUNT_HAS_BEEN_FROZEN_EXP(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, ACCOUNT_HAS_BEEN_FROZEN.message));

    public BlueException exp;

    FinanceCommonException(BlueException exp) {
        this.exp = exp;
    }

}

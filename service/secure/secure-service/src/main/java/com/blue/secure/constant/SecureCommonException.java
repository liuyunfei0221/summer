package com.blue.secure.constant;

import com.blue.base.model.exps.BlueException;

import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.constant.base.ResponseMessage.*;

/**
 * @author liuyunfei
 * @date 2021/11/3
 * @apiNote
 */
@SuppressWarnings("unused")
public enum SecureCommonException {

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
    BLANK_NAME_EXP( new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "name can't be blank")),

    /**
     * BLANK_DESC_EXP
     */
    BLANK_DESC_EXP( new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "description can't be blank")),

    /**
     * ROLE_NAME_ALREADY_EXIST_EXP
     */
    ROLE_NAME_ALREADY_EXIST_EXP(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "The name already exists")),

    /**
     * ROLE_NAME_ALREADY_DEFAULT_EXP
     */
    ROLE_NAME_ALREADY_DEFAULT_EXP(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "role is already default")),

    /**
     * INVALID_ACCT_OR_PWD_EXP
     */
    INVALID_ACCT_OR_PWD_EXP(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_ACCT_OR_PWD.message)),

    /**
     * ACCOUNT_HAS_BEEN_FROZEN_EXP
     */
    ACCOUNT_HAS_BEEN_FROZEN_EXP(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, ACCOUNT_HAS_BEEN_FROZEN.message));

    public BlueException exp;

    SecureCommonException(BlueException exp) {
        this.exp = exp;
    }

}

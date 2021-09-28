package com.blue.base.constant.base;

import com.blue.base.model.base.Access;

import static com.blue.base.constant.base.BlueNumericalValue.*;
import static com.blue.base.constant.secure.DeviceType.UNKNOWN;
import static com.blue.base.constant.secure.LoginType.NOT_LOGGED_IN;

/**
 * 特殊身份
 *
 * @author liuyunfei
 * @date 2021/9/2
 * @apiNote
 */
public enum SpecialAccess {

    /**
     * 游客身份
     */
    VISITOR(new Access(NOT_LOGGED_IN_MEMBER_ID.value, NOT_LOGGED_IN_ROLE_ID.value, NOT_LOGGED_IN.identity, UNKNOWN.identity, NOT_LOGGED_IN_TIME.value), "游客身份");

    public final Access access;

    public final String disc;

    SpecialAccess(Access access, String disc) {
        this.access = access;
        this.disc = disc;
    }
}

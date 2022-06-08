package com.blue.base.constant.common;

import com.blue.base.model.common.Access;

import static com.blue.base.constant.common.BlueNumericalValue.*;
import static com.blue.base.constant.auth.DeviceType.UNKNOWN;
import static com.blue.base.constant.auth.CredentialType.NOT_LOGGED_IN;

/**
 * special access info
 *
 * @author liuyunfei
 */
public enum SpecialAccess {

    /**
     * visitor not login
     */
    VISITOR(new Access(NOT_LOGGED_IN_MEMBER_ID.value, NOT_LOGGED_IN_ROLE_ID.value, NOT_LOGGED_IN.identity, UNKNOWN.identity, NOT_LOGGED_IN_TIME.value), "visitor not login");

    public final Access access;

    public final String disc;

    SpecialAccess(Access access, String disc) {
        this.access = access;
        this.disc = disc;
    }
}

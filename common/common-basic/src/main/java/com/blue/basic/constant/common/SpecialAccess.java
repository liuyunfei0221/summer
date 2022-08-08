package com.blue.basic.constant.common;

import com.blue.basic.model.common.Access;

import static com.blue.basic.constant.auth.CredentialType.NOT_LOGGED_IN;
import static com.blue.basic.constant.auth.DeviceType.UNKNOWN;
import static com.blue.basic.constant.common.BlueCommonThreshold.*;
import static java.util.Collections.singletonList;

/**
 * special access info
 *
 * @author liuyunfei
 */
public enum SpecialAccess {

    /**
     * visitor not session
     */
    VISITOR(new Access(NOT_LOGGED_IN_MEMBER_ID.value, singletonList(NOT_LOGGED_IN_ROLE_ID.value), NOT_LOGGED_IN.identity, UNKNOWN.identity, NOT_LOGGED_IN_TIME.value), "visitor not session");

    public final Access access;

    public final String disc;

    SpecialAccess(Access access, String disc) {
        this.access = access;
        this.disc = disc;
    }
}

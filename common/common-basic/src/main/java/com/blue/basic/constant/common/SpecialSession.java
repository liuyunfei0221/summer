package com.blue.basic.constant.common;

import com.blue.basic.model.common.Session;

import static com.blue.basic.constant.auth.CredentialType.NOT_LOGGED_IN;
import static com.blue.basic.constant.auth.DeviceType.UNKNOWN;
import static com.blue.basic.constant.common.BlueCommonThreshold.*;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_VALUE;
import static java.util.Collections.singletonList;

/**
 * special session info
 *
 * @author liuyunfei
 */
public enum SpecialSession {

    /**
     * visitor not login
     */
    VISITOR(new Session(NOT_LOGGED_IN_MEMBER_ID.value, EMPTY_VALUE.value, singletonList(NOT_LOGGED_IN_ROLE_ID.value), NOT_LOGGED_IN.identity, UNKNOWN.identity, NOT_LOGGED_IN_TIME.value), "visitor not session");

    public final Session session;

    public final String disc;

    SpecialSession(Session session, String disc) {
        this.session = session;
        this.disc = disc;
    }
}

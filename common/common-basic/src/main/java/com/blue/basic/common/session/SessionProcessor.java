package com.blue.basic.common.session;

import com.blue.basic.model.common.Session;
import com.blue.basic.model.exps.BlueException;
import com.google.gson.JsonSyntaxException;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.common.base.CommonFunctions.GSON;
import static com.blue.basic.constant.common.ResponseElement.UNAUTHORIZED;

/**
 * session object and session str converter
 * (session -> json) or (json -> session)
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces"})
public final class SessionProcessor {

    /**
     * session -> json
     *
     * @param session
     * @return
     */
    public static String sessionToJson(Session session) {
        if (isNull(session))
            throw new BlueException(UNAUTHORIZED);

        return GSON.toJson(session);
    }

    /**
     * json -> session
     *
     * @param json
     * @return
     */
    public static Session jsonToSession(String json) {
        if (isNull(json))
            throw new BlueException(UNAUTHORIZED);

        try {
            return GSON.fromJson(json, Session.class);
        } catch (JsonSyntaxException e) {
            throw new BlueException(UNAUTHORIZED);
        }
    }

}
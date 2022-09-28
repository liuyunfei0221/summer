package com.blue.basic.common.session;

import com.blue.basic.model.common.Session;
import com.blue.basic.model.exps.BlueException;
import com.google.gson.JsonSyntaxException;

import static com.blue.basic.common.base.BlueChecker.isNotNull;
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
        if (isNotNull(session))
            return GSON.toJson(session);

        throw new BlueException(UNAUTHORIZED);
    }

    /**
     * json -> session
     *
     * @param json
     * @return
     */
    public static Session jsonToSession(String json) {
        if (isNotNull(json))
            try {
                return GSON.fromJson(json, Session.class);
            } catch (JsonSyntaxException e) {
                throw new BlueException(UNAUTHORIZED);
            }

        throw new BlueException(UNAUTHORIZED);
    }

}
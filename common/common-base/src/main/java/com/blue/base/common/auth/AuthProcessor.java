package com.blue.base.common.auth;

import com.blue.base.model.base.Access;
import com.blue.base.model.exps.BlueException;
import com.google.gson.JsonSyntaxException;

import static com.blue.base.common.base.CommonFunctions.GSON;
import static com.blue.base.constant.base.ResponseElement.UNAUTHORIZED;

/**
 * auth object and auth str converter
 * (auth -> json) or (json -> auth)
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces"})
public final class AuthProcessor {

    /**
     * access -> json
     *
     * @param access
     * @return
     */
    public static String accessToJson(Access access) {
        if (access != null)
            return GSON.toJson(access);

        throw new BlueException(UNAUTHORIZED);
    }

    /**
     * json -> access
     *
     * @param json
     * @return
     */
    public static Access jsonToAccess(String json) {
        if (json != null)
            try {
                return GSON.fromJson(json, Access.class);
            } catch (JsonSyntaxException e) {
                throw new BlueException(UNAUTHORIZED);
            }

        throw new BlueException(UNAUTHORIZED);
    }

}
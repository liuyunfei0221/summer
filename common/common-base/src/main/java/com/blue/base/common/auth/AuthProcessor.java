package com.blue.base.common.auth;

import com.blue.base.common.base.CommonFunctions;
import com.blue.base.model.base.Access;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import static com.blue.base.constant.base.CommonException.UNAUTHORIZED_EXP;

/**
 * auth object and auth str converter
 * (auth -> json) or (json -> auth)
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces"})
public final class AuthProcessor {

    private static final Gson GSON = CommonFunctions.GSON;

    /**
     * access -> json
     *
     * @param access
     * @return
     */
    public static String accessToJson(Access access) {
        if (access != null)
            return GSON.toJson(access);

        throw UNAUTHORIZED_EXP.exp;
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
                throw UNAUTHORIZED_EXP.exp;
            }

        throw UNAUTHORIZED_EXP.exp;
    }

}
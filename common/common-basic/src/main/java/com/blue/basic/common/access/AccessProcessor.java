package com.blue.basic.common.access;

import com.blue.basic.model.common.Access;
import com.blue.basic.model.exps.BlueException;
import com.google.gson.JsonSyntaxException;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.GSON;
import static com.blue.basic.constant.common.ResponseElement.UNAUTHORIZED;

/**
 * access object and access str converter
 * (auth -> json) or (json -> auth)
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces"})
public final class AccessProcessor {

    /**
     * access -> json
     *
     * @param access
     * @return
     */
    public static String accessToJson(Access access) {
        if (isNull(access))
            throw new BlueException(UNAUTHORIZED);

        return GSON.toJson(access);
    }

    /**
     * json -> access
     *
     * @param json
     * @return
     */
    public static Access jsonToAccess(String json) {
        if (isBlank(json))
            throw new BlueException(UNAUTHORIZED);

        try {
            return GSON.fromJson(json, Access.class);
        } catch (JsonSyntaxException e) {
            throw new BlueException(UNAUTHORIZED);
        }
    }

}
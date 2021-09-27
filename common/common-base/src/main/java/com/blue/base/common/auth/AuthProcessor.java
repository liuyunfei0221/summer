package com.blue.base.common.auth;

import com.blue.base.common.base.CommonFunctions;
import com.blue.base.model.base.Access;
import com.blue.base.model.exps.BlueException;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import static com.blue.base.constant.base.ResponseElement.UNAUTHORIZED;

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
     * 认证信息转换为打到下游的成员信息
     *
     * @param access
     * @return
     */
    public static String accessToJson(Access access) {
        if (access != null)
            return GSON.toJson(access);

        throw new RuntimeException("access不能为空");
    }

    /**
     * 成员信息转为实体封装
     *
     * @param json
     * @return
     */
    public static Access jsonToAccess(String json) {
        if (json != null)
            try {
                return GSON.fromJson(json, Access.class);
            } catch (JsonSyntaxException e) {
                throw new BlueException(UNAUTHORIZED.status, UNAUTHORIZED.code, UNAUTHORIZED.message);
            }

        throw new BlueException(UNAUTHORIZED.status, UNAUTHORIZED.code, UNAUTHORIZED.message);
    }

}
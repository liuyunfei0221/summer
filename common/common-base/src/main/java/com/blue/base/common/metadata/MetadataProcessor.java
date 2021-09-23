package com.blue.base.common.metadata;

import com.blue.base.common.base.CommonFunctions;
import com.blue.base.model.exps.BlueException;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.google.gson.reflect.TypeToken.getParameterized;

/**
 * 认证信息到用户信息转换
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces"})
public final class MetadataProcessor {

    private static final Gson GSON = CommonFunctions.GSON;

    private static final Type METADATA_TYPE = getParameterized(Map.class, String.class, String.class).getType();

    /**
     * 返回空metadata
     */
    private static final Supplier<Map<String, String>> EMPTY_METADATA_SUP = HashMap::new;

    /**
     * 元数据信息转JSON
     *
     * @param metadata
     * @return
     */
    public static String metadataToJson(Map<String, String> metadata) {
        if (metadata != null)
            return GSON.toJson(metadata);

        throw new RuntimeException("metadata不能为空");
    }

    /**
     * json元数据转换为map
     *
     * @param json
     * @return
     */
    public static Map<String, String> jsonToMetadata(String json) {
        if (json != null)
            try {
                return GSON.fromJson(json, METADATA_TYPE);
            } catch (JsonSyntaxException e) {
                throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "metadata格式错误");
            }

        return EMPTY_METADATA_SUP.get();
    }

}

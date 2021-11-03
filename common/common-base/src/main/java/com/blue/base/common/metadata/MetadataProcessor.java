package com.blue.base.common.metadata;

import com.blue.base.common.base.CommonFunctions;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static com.blue.base.constant.base.CommonException.INVALID_METADATA_PARAM_EXP;
import static com.google.gson.reflect.TypeToken.getParameterized;

/**
 * metadata converter
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces"})
public final class MetadataProcessor {

    private static final Gson GSON = CommonFunctions.GSON;

    private static final Type METADATA_TYPE = getParameterized(Map.class, String.class, String.class).getType();

    /**
     * return empty metadata
     */
    private static final Supplier<Map<String, String>> EMPTY_METADATA_SUP = HashMap::new;

    /**
     * metadata map -> json
     *
     * @param metadata
     * @return
     */
    public static String metadataToJson(Map<String, String> metadata) {
        if (metadata != null)
            return GSON.toJson(metadata);

        throw INVALID_METADATA_PARAM_EXP.exp;
    }

    /**
     * json -> metadata map
     *
     * @param json
     * @return
     */
    public static Map<String, String> jsonToMetadata(String json) {
        if (json != null)
            try {
                return GSON.fromJson(json, METADATA_TYPE);
            } catch (JsonSyntaxException e) {
                throw INVALID_METADATA_PARAM_EXP.exp;
            }

        return EMPTY_METADATA_SUP.get();
    }

}

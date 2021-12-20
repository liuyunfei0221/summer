package com.blue.base.common.metadata;

import com.blue.base.model.exps.BlueException;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static com.blue.base.common.base.CommonFunctions.GSON;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.google.gson.reflect.TypeToken.getParameterized;

/**
 * metadata converter
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces"})
public final class MetadataProcessor {

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

        throw new BlueException(INVALID_METADATA_PARAM.status, INVALID_METADATA_PARAM.code, INVALID_METADATA_PARAM.message);
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
                throw new BlueException(INVALID_METADATA_PARAM.status, INVALID_METADATA_PARAM.code, INVALID_METADATA_PARAM.message);
            }

        return EMPTY_METADATA_SUP.get();
    }

}

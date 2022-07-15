package com.blue.basic.common.metadata;

import com.blue.basic.model.exps.BlueException;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static com.blue.basic.common.base.BlueChecker.isNotNull;
import static com.blue.basic.common.base.CommonFunctions.GSON;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.google.gson.reflect.TypeToken.getParameterized;

/**
 * metadata converter
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces"})
public final class MetadataProcessor {

    private static final Type METADATA_TYPE = getParameterized(Map.class, String.class, String.class).getType();

    /**
     * return empty metadata
     */
    private static final Supplier<Map<String, String>> EMPTY_METADATA_SUP = HashMap::new;

    /**
     * empty json
     */
    private static final String EMPTY_JSON = "{}";

    /**
     * metadata map -> json
     *
     * @param metadata
     * @return
     */
    public static String metadataToJson(Map<String, String> metadata) {
        if (isNotNull(metadata))
            return GSON.toJson(metadata);

        return EMPTY_JSON;
    }

    /**
     * json -> metadata map
     *
     * @param json
     * @return
     */
    public static Map<String, String> jsonToMetadata(String json) {
        if (isNotNull(json))
            try {
                return GSON.fromJson(json, METADATA_TYPE);
            } catch (JsonSyntaxException e) {
                throw new BlueException(INVALID_METADATA_PARAM);
            }

        return EMPTY_METADATA_SUP.get();
    }

}

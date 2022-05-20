package com.blue.base.common.turing;

import com.blue.base.model.common.TuringData;
import com.blue.base.model.exps.BlueException;
import com.google.gson.JsonSyntaxException;

import static com.blue.base.common.base.BlueChecker.isNotNull;
import static com.blue.base.common.base.CommonFunctions.GSON;
import static com.blue.base.constant.base.ResponseElement.*;

/**
 * turing data object and json str converter
 * (turing data -> json) or (json -> turing data)
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces"})
public final class TuringDataProcessor {

    /**
     * turing data -> json
     *
     * @param turingData
     * @return
     */
    public static String turingDataToJson(TuringData turingData) {
        if (isNotNull(turingData))
            return GSON.toJson(turingData);

        throw new BlueException(NEED_TURING_TEST);
    }

    /**
     * json -> turing data
     *
     * @param json
     * @return
     */
    public static TuringData jsonToTuringData(String json) {
        if (isNotNull(json))
            try {
                return GSON.fromJson(json, TuringData.class);
            } catch (JsonSyntaxException e) {
                throw new BlueException(NEED_TURING_TEST);
            }

        throw new BlueException(NEED_TURING_TEST);
    }

}
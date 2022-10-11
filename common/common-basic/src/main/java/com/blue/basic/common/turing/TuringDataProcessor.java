package com.blue.basic.common.turing;

import com.blue.basic.model.common.TuringData;
import com.blue.basic.model.exps.BlueException;
import com.google.gson.JsonSyntaxException;

import static com.blue.basic.common.base.BlueChecker.isNotBlank;
import static com.blue.basic.common.base.CommonFunctions.GSON;
import static com.blue.basic.constant.common.ResponseElement.NEED_TURING_TEST;
import static java.util.Optional.ofNullable;

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
        return GSON.toJson(ofNullable(turingData).orElseGet(TuringData::new));
    }

    /**
     * json -> turing data
     *
     * @param json
     * @return
     */
    public static TuringData jsonToTuringData(String json) {
        if (isNotBlank(json))
            try {
                return GSON.fromJson(json, TuringData.class);
            } catch (JsonSyntaxException e) {
                throw new BlueException(NEED_TURING_TEST);
            }

        return new TuringData();
    }

}
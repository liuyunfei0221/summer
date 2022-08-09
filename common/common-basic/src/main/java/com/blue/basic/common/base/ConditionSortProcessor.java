package com.blue.basic.common.base;

import com.blue.basic.model.common.SortCondition;
import com.blue.basic.model.exps.BlueException;

import java.util.Map;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.ConstantProcessor.assertSortType;
import static com.blue.basic.constant.common.ResponseElement.INVALID_PARAM;
import static com.blue.basic.constant.common.SortType.DESC;
import static java.util.Optional.ofNullable;

/**
 * sort by condition util
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
public final class ConditionSortProcessor {

    /**
     * assert and package sort attr
     *
     * @param condition
     * @param sortAttrMapping
     * @param defaultSortAttr
     */
    public static void process(SortCondition condition, Map<String, String> sortAttrMapping, String defaultSortAttr) {
        if (isNull(condition))
            return;

        String sortType = condition.getSortType();
        assertSortType(sortType, true);
        if (isBlank(sortType))
            condition.setSortType(DESC.identity);

        String sortAttribute = condition.getSortAttribute();
        if (isNotBlank(sortAttribute) && isNotEmpty(sortAttrMapping)) {
            condition.setSortAttribute(ofNullable(sortAttrMapping.get(sortAttribute))
                    .orElseThrow(() -> new BlueException(INVALID_PARAM)));
        } else {
            condition.setSortAttribute(isNotBlank(defaultSortAttr) ? defaultSortAttr : null);
        }
    }

}

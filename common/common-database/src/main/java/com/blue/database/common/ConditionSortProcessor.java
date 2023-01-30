package com.blue.database.common;

import com.blue.basic.model.common.SortCondition;
import com.blue.basic.model.exps.BlueException;

import java.util.Map;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.ConstantProcessor.assertSortType;
import static com.blue.basic.constant.common.ResponseElement.INVALID_PARAM;
import static com.blue.basic.constant.common.SortType.DESC;
import static java.util.Optional.ofNullable;

/**
 * sort processor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
public final class ConditionSortProcessor {

    public static final String DEFAULT_SORT_TYPE_IDENTITY = DESC.identity;

    /**
     * assert and package sort attr
     *
     * @param condition
     * @param sortAttrMapping
     * @param defaultSortColumn
     */
    public static void process(SortCondition condition, Map<String, String> sortAttrMapping, String defaultSortColumn) {
        if (isNull(condition))
            return;

        String sortType = condition.getSortType();
        if (isBlank(sortType))
            condition.setSortType(DEFAULT_SORT_TYPE_IDENTITY);
        assertSortType(sortType, false);

        String sortAttribute = condition.getSortAttribute();
        if (isNotBlank(sortAttribute) && isNotEmpty(sortAttrMapping)) {
            condition.setSortAttribute(ofNullable(sortAttrMapping.get(sortAttribute))
                    .orElseThrow(() -> new BlueException(INVALID_PARAM)));
        } else {
            condition.setSortAttribute(isNotBlank(defaultSortColumn) ? defaultSortColumn : null);
        }
    }

}

package com.blue.base.common.base;

import com.blue.base.model.base.SortCondition;
import com.blue.base.model.exps.BlueException;

import java.util.Map;

import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.common.base.ConstantProcessor.assertSortType;
import static com.blue.base.constant.base.ResponseElement.INVALID_PARAM;
import static com.blue.base.constant.base.SortType.DESC;
import static java.util.Optional.ofNullable;

/**
 * sort by condition util
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
public final class ConditionSortProcessor {

    /**
     * assert and package attr
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
        if (isBlank(sortAttribute) || isEmpty(sortAttrMapping))
            condition.setSortAttribute(isNotBlank(defaultSortAttr) ? defaultSortAttr : null);

        condition.setSortAttribute(ofNullable(sortAttrMapping.get(sortAttribute))
                .orElseThrow(() -> new BlueException(INVALID_PARAM)));
    }

}

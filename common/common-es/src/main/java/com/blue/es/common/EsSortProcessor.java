package com.blue.es.common;


import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOptionsBuilders;
import co.elastic.clients.elasticsearch._types.SortOrder;
import com.blue.basic.model.common.SortCondition;
import com.blue.basic.model.exps.BlueException;
import com.blue.es.constant.SortSchema;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.constant.common.ResponseElement.INVALID_PARAM;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;

/**
 * es sort processor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavadocDeclaration"})
public final class EsSortProcessor {

    private static final SortOptions DEFAULT_SORT_OPTIONS = SortOptions.of(builder -> builder.score(SortOptionsBuilders.score().order(SortOrder.Desc).build()));

    private static final Map<String, Function<String, SortOptions>> MAPPING =
            Stream.of(SortSchema.values()).collect(toMap(e -> e.sortType.identity, e -> e.converter, (a, b) -> a));

    private static final BiFunction<String, String, SortOptions> CONVERTER = (sort, attr) ->
            isNotBlank(attr) ?
                    ofNullable(sort)
                            .map(MAPPING::get)
                            .map(c -> c.apply(attr))
                            .orElse(DEFAULT_SORT_OPTIONS)
                    :
                    DEFAULT_SORT_OPTIONS;

    /**
     * assert and package sort attr
     *
     * @param sort
     * @param attr
     * @return
     */
    public static SortOptions process(String sort, String attr) {
        return CONVERTER.apply(sort, attr);
    }

    /**
     * assert and package sort attr
     *
     * @param condition
     * @param sortAttrMapping
     * @param defaultSortAttr
     */
    public static SortOptions process(SortCondition condition, Map<String, String> sortAttrMapping, String defaultSortAttr) {
        if (isNull(condition))
            return DEFAULT_SORT_OPTIONS;

        String sortAttribute = condition.getSortAttribute();
        if (isBlank(sortAttribute)) {
            condition.setSortAttribute(defaultSortAttr);
        } else {
            if (!sortAttrMapping.containsValue(sortAttribute))
                throw new BlueException(INVALID_PARAM);
        }

        return process(condition.getSortType(), condition.getSortAttribute());
    }

}

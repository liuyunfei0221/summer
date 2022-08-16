package com.blue.mongo.common;


import com.blue.basic.model.common.SortCondition;
import com.blue.basic.model.exps.BlueException;
import com.blue.mongo.constant.SortSchema;
import org.springframework.data.domain.Sort;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.constant.common.ResponseElement.INVALID_PARAM;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;

/**
 * mongo sort processor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavadocDeclaration"})
public final class MongoSortProcessor {

    private static final Sort DEFAULT_SORT = Sort.unsorted();

    private static final Map<String, Function<String, Sort>> MAPPING =
            Stream.of(SortSchema.values()).collect(toMap(e -> e.sortType.identity, e -> e.converter, (a, b) -> a));

    private static final BiFunction<String, String, Sort> CONVERTER = (sort, attr) ->
            isNotBlank(attr) ?
                    ofNullable(sort)
                            .map(MAPPING::get)
                            .map(c -> c.apply(attr))
                            .orElse(DEFAULT_SORT)
                    :
                    DEFAULT_SORT;

    /**
     * assert and package sort attr
     *
     * @param sort
     * @param attr
     * @return
     */
    public static Sort process(String sort, String attr) {
        return CONVERTER.apply(sort, attr);
    }

    /**
     * assert and package sort attr
     *
     * @param condition
     * @param sortAttrMapping
     * @param defaultSortAttr
     */
    public static Sort process(SortCondition condition, Map<String, String> sortAttrMapping, String defaultSortAttr) {
        if (isNull(condition))
            return DEFAULT_SORT;

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

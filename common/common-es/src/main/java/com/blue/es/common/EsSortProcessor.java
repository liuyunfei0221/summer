package com.blue.es.common;


import co.elastic.clients.elasticsearch._types.SortOptions;
import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.model.common.SortCondition;
import com.blue.basic.model.exps.BlueException;
import com.blue.es.constant.SortSchema;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;
import static com.blue.es.constant.SortSchema.DESC;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;

/**
 * es sort processor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavadocDeclaration"})
public final class EsSortProcessor {

    private static final String DEFAULT_SORT = DESC.sortType.identity;

    private static final Map<String, Function<String, SortOptions>> MAPPING =
            Stream.of(SortSchema.values()).collect(toMap(e -> e.sortType.identity, e -> e.converter, (a, b) -> a));

    private static final BiFunction<String, String, SortOptions> PROCESSOR = (sort, attr) -> {
        if (isNotBlank(attr))
            return ofNullable(MAPPING.get(ofNullable(sort).filter(BlueChecker::isNotBlank).orElse(DEFAULT_SORT)))
                    .map(p -> p.apply(attr))
                    .orElseThrow(() -> new BlueException(INVALID_IDENTITY));

        throw new BlueException(INVALID_IDENTITY);
    };

    /**
     * assert and package sort attr
     *
     * @param sort
     * @param attr
     * @return
     */
    public static SortOptions process(String sort, String attr) {
        return PROCESSOR.apply(sort, attr);
    }

    /**
     * assert and package sort attr
     *
     * @param condition
     * @param sortAttrMapping
     * @param defaultSortAttr
     */
    public static SortOptions process(SortCondition condition, Map<String, String> sortAttrMapping, String defaultSortAttr) {
        return isNotNull(condition) ?
                process(ofNullable(condition.getSortType()).filter(BlueChecker::isNotBlank).orElse(DEFAULT_SORT),
                        ofNullable(sortAttrMapping).filter(BlueChecker::isNotEmpty).map(sam -> sam.get(condition.getSortAttribute())).filter(BlueChecker::isNotBlank).orElse(defaultSortAttr))
                :
                process(DEFAULT_SORT, defaultSortAttr);
    }

}

package com.blue.mongo.common;


import com.blue.basic.model.common.SortCondition;
import com.blue.basic.model.exps.BlueException;
import com.blue.mongo.constant.SortSchema;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.constant.common.ResponseElement.INVALID_PARAM;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;
import static org.springframework.data.domain.Sort.unsorted;

/**
 * sort converter
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavadocDeclaration"})
public final class SortConverter {

    private static final Map<String, Function<List<String>, Sort>> MAPPING =
            Stream.of(SortSchema.values()).collect(toMap(e -> e.sortType.identity, e -> e.converter, (a, b) -> a));

    private static final BiFunction<String, List<String>, Sort> CONVERTER = (sort, attrs) ->
            isNotEmpty(attrs) ?
                    ofNullable(sort)
                            .map(MAPPING::get)
                            .map(c -> c.apply(attrs))
                            .orElseGet(Sort::unsorted)
                    :
                    Sort.unsorted();

    /**
     * convert to sort
     *
     * @param sort
     * @param attrs
     * @return
     */
    public static Sort convert(String sort, List<String> attrs) {
        return CONVERTER.apply(sort, attrs);
    }

    /**
     * convert to sort
     *
     * @param condition
     * @param sortAttrMapping
     * @param defaultSortAttr
     */
    public static Sort convert(SortCondition condition, Map<String, String> sortAttrMapping, String defaultSortAttr) {
        if (isNull(condition))
            return unsorted();

        String sortAttribute = condition.getSortAttribute();
        if (isBlank(sortAttribute)) {
            condition.setSortAttribute(defaultSortAttr);
        } else {
            if (!sortAttrMapping.containsValue(sortAttribute))
                throw new BlueException(INVALID_PARAM);
        }

        return convert(condition.getSortType(), singletonList(condition.getSortAttribute()));
    }

}

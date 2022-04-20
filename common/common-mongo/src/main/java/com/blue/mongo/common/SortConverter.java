package com.blue.mongo.common;


import com.blue.mongo.constant.SortSchema;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.blue.base.common.base.BlueChecker.isNotEmpty;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;

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

}

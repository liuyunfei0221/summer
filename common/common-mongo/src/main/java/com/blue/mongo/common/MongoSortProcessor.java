package com.blue.mongo.common;


import com.blue.basic.model.common.SortElement;
import com.blue.basic.model.exps.BlueException;
import com.blue.mongo.constant.SortSchema;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;

/**
 * mongo sort processor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavadocDeclaration"})
public final class MongoSortProcessor {

    private static final Map<String, Function<String, Sort.Order>> MAPPING =
            Stream.of(SortSchema.values()).collect(toMap(e -> e.sortType.identity, e -> e.sortOrder, (a, b) -> a));

    private static final Function<String, Function<String, Sort.Order>> PROCESSOR = sortType -> {
        if (isNotBlank(sortType))
            return ofNullable(MAPPING.get(sortType)).orElseThrow(() -> new BlueException(INVALID_IDENTITY));

        throw new BlueException(INVALID_IDENTITY);
    };

    /**
     * package sort attr
     *
     * @param sorts
     * @return
     */
    public static Sort process(List<SortElement> sorts) {
        if (isEmpty(sorts))
            return Sort.unsorted();

        List<Sort.Order> orders = new ArrayList<>(sorts.size());

        String sortAttribute;
        String sortType;

        for (SortElement sortElement : sorts) {
            if (isNull(sortElement))
                continue;

            sortAttribute = sortElement.getSortAttribute();
            sortType = sortElement.getSortType();

            if (isNotBlank(sortAttribute) && isNotBlank(sortType))
                orders.add(PROCESSOR.apply(sortType).apply(sortAttribute));
        }

        return isNotEmpty(orders) ? Sort.by(orders) : Sort.unsorted();
    }

}

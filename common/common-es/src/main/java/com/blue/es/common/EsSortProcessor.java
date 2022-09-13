package com.blue.es.common;


import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import com.blue.basic.model.common.SortElement;
import com.blue.basic.model.exps.BlueException;
import com.blue.es.constant.SortSchema;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.blue.basic.common.base.BlueChecker.isNotBlank;
import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;

/**
 * es sort processor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavadocDeclaration"})
public final class EsSortProcessor {

    private static final Map<String, SortOrder> MAPPING =
            Stream.of(SortSchema.values()).collect(toMap(e -> e.sortType.identity, e -> e.sortOrder, (a, b) -> a));

    private static final Function<String, SortOrder> PROCESSOR = sortType ->
            ofNullable(MAPPING.get(sortType)).orElseThrow(() -> new BlueException(INVALID_IDENTITY));


    /**
     * package sort attr
     *
     * @param sortElements
     * @return
     */
    public static SortOptions process(List<SortElement> sortElements) {

        return SortOptions.of(builder -> builder.field(FieldSort.of(b -> {
            String sortAttribute;
            String sortType;

            for (SortElement sortElement : sortElements) {
                if (isNull(sortElement))
                    continue;

                if (isNotBlank(sortAttribute = sortElement.getSortAttribute()) && isNotBlank(sortType = sortElement.getSortType()))
                    b.field(sortAttribute).order(PROCESSOR.apply(sortType));
            }

            return b;
        })));
    }

}

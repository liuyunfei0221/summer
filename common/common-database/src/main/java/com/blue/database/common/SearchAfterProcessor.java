package com.blue.database.common;


import com.blue.basic.constant.common.SortType;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.ConstantProcessor.assertSortType;
import static com.blue.basic.constant.common.SortType.DESC;
import static java.util.Optional.ofNullable;

/**
 * search after processor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavadocDeclaration", "unused"})
public final class SearchAfterProcessor {

    private static final String DEFAULT_SORT = DESC.identity;

    /**
     * parse data and search after
     *
     * @param data
     * @param sortType
     * @param searchAfterParser
     * @param <T>
     * @param <A>
     * @return
     */
    public static <T extends Serializable, A extends Serializable> A parseSearchAfter(List<T> data, String sortType, Function<T, A> searchAfterParser) {
        if (isEmpty(data) || isNull(searchAfterParser))
            return null;

        T t;
        A searchAfter;

        assertSortType(sortType, true);
        String tarSortType = ofNullable(sortType).orElse(DEFAULT_SORT);

        if (SortType.DESC.identity.equals(tarSortType)) {
            int index = data.size() - 1;
            for (int i = index; i >= 0; i--)
                if (isNotNull(t = data.get(i)) && isNotNull(searchAfter = searchAfterParser.apply(t)))
                    return searchAfter;
        } else if (SortType.ASC.identity.equals(tarSortType)) {
            int index = data.size() - 1;
            for (int i = index; i >= 0; i--)
                if (isNotNull(t = data.get(i)) && isNotNull(searchAfter = searchAfterParser.apply(t)))
                    return searchAfter;
        } else {
            return null;
        }

        return null;
    }

}

package com.blue.mongo.common;


import com.blue.basic.constant.common.SortType;
import org.springframework.data.mongodb.core.query.Query;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.ConstantProcessor.assertSortType;
import static com.blue.mongo.constant.SortSchema.DESC;
import static java.util.Optional.ofNullable;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * mongo search after processor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavadocDeclaration", "unused"})
public final class MongoSearchAfterProcessor<T extends Serializable, A extends Serializable> {

    private static final String DEFAULT_SORT = DESC.sortType.identity;

    /**
     * package search after to query
     *
     * @param query
     * @param sortType
     * @param sortAttribute
     * @param searchAfter
     * @param <A>
     */
    public static <A extends Serializable> void packageSearchAfter(Query query, String sortType, String sortAttribute, A searchAfter) {
        if (isNull(query) || isNotBlank(sortAttribute) || isNull(searchAfter))
            return;

        assertSortType(sortType, true);
        String tarSortType = ofNullable(sortType).orElse(DEFAULT_SORT);

        if (SortType.DESC.identity.equals(tarSortType)) {
            query.addCriteria(where(sortAttribute).lt(searchAfter));
        } else if (SortType.ASC.identity.equals(tarSortType)) {
            query.addCriteria(where(sortAttribute).gt(searchAfter));
        }
    }

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

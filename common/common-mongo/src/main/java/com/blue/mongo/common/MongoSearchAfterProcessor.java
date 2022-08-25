package com.blue.mongo.common;


import com.blue.basic.common.base.BlueChecker;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.mongo.constant.SortSchema.ASC;
import static com.blue.mongo.constant.SortSchema.DESC;
import static java.util.Optional.ofNullable;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * es search after processor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavadocDeclaration", "unused"})
public final class MongoSearchAfterProcessor {

    private static final String DEFAULT_SORT = DESC.sortType.identity;

    private static final Map<String, BiFunction<String, Object, Criteria>> MAPPING = new HashMap<>(2, 2.0f);

    static {
        MAPPING.put(DESC.sortType.identity, (attr, searchAfter) ->
                isNotBlank(attr) && isNotNull(searchAfter) ? where(attr).lt(searchAfter) : null);

        MAPPING.put(ASC.sortType.identity, (attr, searchAfter) ->
                isNotBlank(attr) && isNotNull(searchAfter) ? where(attr).gt(searchAfter) : null);
    }

    private static final Function<String, BiFunction<String, Object, Criteria>> CRITERIA_GENERATOR_GETTER = sort ->
            ofNullable(sort).map(MAPPING::get).orElse(null);

    /**
     * package search after to query
     *
     * @param query
     * @param sortAttribute
     * @param searchAfter
     * @param <A>
     */
    public static <A extends Serializable> void packageSearchAfter(Query query, String sortAttribute, A searchAfter) {
        if (isNotNull(query) && isNotBlank(sortAttribute) && isNotNull(searchAfter))
            ofNullable(CRITERIA_GENERATOR_GETTER.apply(DEFAULT_SORT)).map(g -> g.apply(sortAttribute, searchAfter))
                    .ifPresent(query::addCriteria);
    }

    /**
     * package search after to query
     *
     * @param query
     * @param sort
     * @param sortAttribute
     * @param searchAfter
     * @param <A>
     */
    public static <A extends Serializable> void packageSearchAfter(Query query, String sort, String sortAttribute, A searchAfter) {
        if (isNotNull(query) && isNotBlank(sortAttribute) && isNotNull(searchAfter))
            ofNullable(CRITERIA_GENERATOR_GETTER.apply(ofNullable(sort).filter(BlueChecker::isNotBlank).orElse(DEFAULT_SORT))).map(g -> g.apply(sortAttribute, searchAfter))
                    .ifPresent(query::addCriteria);
    }

    /**
     * parse data and search after
     *
     * @param data
     * @param searchAfterParser
     * @param <T>
     * @param <A>
     * @return
     */
    public static <T extends Serializable, A extends Serializable> A parseSearchAfter(List<T> data, Function<T, A> searchAfterParser) {
        if (isEmpty(data) || isNull(searchAfterParser))
            return null;

        int index = data.size() - 1;

        T t;
        A searchAfter;
        for (int i = 0; index >= i; index--)
            if (isNotNull(t = data.get(index)) && isNotNull(searchAfter = searchAfterParser.apply(t)))
                return searchAfter;

        return null;
    }

}

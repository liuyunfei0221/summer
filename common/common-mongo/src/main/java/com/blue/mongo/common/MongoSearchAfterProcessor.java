package com.blue.mongo.common;


import com.blue.basic.model.common.DataAndSearchAfter;
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

    private static final Map<String, BiFunction<String, Object, Criteria>> MAPPING = new HashMap<>(2, 2.0f);

    static {
        MAPPING.put(DESC.sortType.identity, (attr, searchAfter) ->
                isNotBlank(attr) && isNotNull(searchAfter) ? where(attr).lte(searchAfter) : null);

        MAPPING.put(ASC.sortType.identity, (attr, searchAfter) ->
                isNotBlank(attr) && isNotNull(searchAfter) ? where(attr).gte(searchAfter) : null);
    }

    private static final Function<String, BiFunction<String, Object, Criteria>> CRITERIA_GENERATOR_GETTER = sort ->
            ofNullable(sort).map(MAPPING::get).orElse(null);


    /**
     * package search after to query
     *
     * @param query
     * @param searchAfter
     */
    public static <A extends Serializable> void packageSearchAfter(Query query, String sort, String sortAttribute, A searchAfter) {
        if (isNotNull(query) && isNotBlank(sort) && isNotBlank(sortAttribute) && isNotNull(searchAfter))
            ofNullable(CRITERIA_GENERATOR_GETTER.apply(sort)).map(g -> g.apply(sortAttribute, searchAfter))
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
    public static <T extends Serializable, A extends Serializable> DataAndSearchAfter<T, A> parseSearchAfter(List<T> data, Function<T, A> searchAfterParser) {
        if (isEmpty(data) || isNull(searchAfterParser))
            return new DataAndSearchAfter<>();

        SearchAfterHolder<A> searchAfterHolder = new SearchAfterHolder<>();

        for (T t : data)
            ofNullable(t).map(searchAfterParser).ifPresent(searchAfter -> searchAfterHolder.searchAfter = searchAfter);

        return new DataAndSearchAfter<>(data, searchAfterHolder.searchAfter);
    }

    /**
     * search after holder
     */
    @SuppressWarnings("unused")
    private static final class SearchAfterHolder<A extends Serializable> {

        /**
         * search after
         */
        private A searchAfter;

        public SearchAfterHolder() {
            this.searchAfter = null;
        }

        public SearchAfterHolder(A searchAfter) {
            this.searchAfter = searchAfter;
        }
    }

}

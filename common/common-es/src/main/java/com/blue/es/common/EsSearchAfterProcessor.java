package com.blue.es.common;


import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.util.ObjectBuilder;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

import static com.blue.basic.common.base.BlueChecker.*;
import static java.util.Collections.emptyList;

/**
 * es search after processor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavadocDeclaration", "unused", "DuplicatedCode"})
public final class EsSearchAfterProcessor {

    /**
     * package search after to query
     *
     * @param builder
     * @param searchAfter
     */
    public static void packageSearchAfter(SearchRequest.Builder builder, List<FieldValue> searchAfter) {
        if (isNotNull(builder) && isNotEmpty(searchAfter))
            builder.searchAfter(searchAfter);
    }

    /**
     * package search after to query
     *
     * @param builder
     * @param searchAfter
     */
    public static void packageSearchAfter(SearchRequest.Builder builder, FieldValue searchAfter) {
        if (isNotNull(builder) && isNotNull(searchAfter))
            builder.searchAfter(searchAfter);
    }

    /**
     * package search after to query
     *
     * @param builder
     * @param fn
     */
    public static void packageSearchAfter(SearchRequest.Builder builder, Function<FieldValue.Builder, ObjectBuilder<FieldValue>> fn) {
        if (isNotNull(builder) && isNotNull(fn))
            builder.searchAfter(fn);
    }

    /**
     * parse data and search after
     *
     * @param hits
     * @param <T>
     * @return
     */
    public static <T extends Serializable> List<FieldValue> parseSearchAfter(List<Hit<T>> hits) {
        if (isEmpty(hits))
            return emptyList();

        int index = hits.size() - 1;

        Hit<T> hit;
        List<FieldValue> searchAfter;
        for (int i = 0; index >= i; index--)
            if (isNotNull(hit = hits.get(index)) && isNotEmpty(searchAfter = hit.sort()))
                return searchAfter;

        return emptyList();
    }

}

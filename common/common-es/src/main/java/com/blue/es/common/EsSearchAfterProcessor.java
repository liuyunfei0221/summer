package com.blue.es.common;


import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.search.Hit;

import java.io.Serializable;
import java.util.List;

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
    public static void packageSearchAfter(SearchRequest.Builder builder, List<String> searchAfter) {
        if (isNotNull(builder) && isNotEmpty(searchAfter))
            builder.searchAfter(searchAfter);
    }

    /**
     * parse data and search after
     *
     * @param hits
     * @param <T>
     * @return
     */
    public static <T extends Serializable> List<String> parseSearchAfter(List<Hit<T>> hits) {
        if (isEmpty(hits))
            return emptyList();

        int index = hits.size() - 1;

        Hit<T> hit;
        List<String> searchAfter;
        for (int i = 0; index >= i; index--)
            if (isNotNull(hit = hits.get(index)) && isNotEmpty(searchAfter = hit.sort()))
                return searchAfter;

        return emptyList();
    }

}

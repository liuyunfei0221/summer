package com.blue.es.common;


import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.blue.basic.common.base.BlueChecker;
import com.blue.es.model.EsDataAndSearchAfter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.blue.basic.common.base.BlueChecker.*;
import static java.util.Optional.ofNullable;

/**
 * es search after processor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavadocDeclaration", "unused", "DuplicatedCode"})
public final class EsPitSearchAfterProcessor {

    /**
     * package search after to query
     *
     * @param builder
     * @param searchAfters
     */
    public static void packagePitSearchAfter(SearchRequest.Builder builder, List<String> searchAfters) {
        if (isNotNull(builder) && isNotEmpty(searchAfters))
            builder.searchAfter(searchAfters);
    }

    /**
     * parse data and search after
     *
     * @param hits
     * @param <T>
     * @return
     */
    public static <T extends Serializable> EsDataAndSearchAfter<T> parsePitSearchAfter(List<Hit<T>> hits) {
        if (isEmpty(hits))
            return new EsDataAndSearchAfter<>();

        List<T> data = new ArrayList<>(hits.size());
        SearchAftersHolder searchAfterHolder = new SearchAftersHolder();

        for (Hit<T> hit : hits)
            ofNullable(hit)
                    .ifPresent(h -> {
                        ofNullable(h.source()).ifPresent(data::add);
                        ofNullable(h.sort()).filter(BlueChecker::isNotEmpty)
                                .ifPresent(searchAfters -> searchAfterHolder.searchAfters = searchAfters);
                    });

        return new EsDataAndSearchAfter<>(data, searchAfterHolder.searchAfters);
    }

    /**
     * search after holder
     */
    private static final class SearchAftersHolder {

        /**
         * search after
         */
        private List<String> searchAfters;

        public SearchAftersHolder() {
            this.searchAfters = null;
        }

        public SearchAftersHolder(List<String> searchAfters) {
            this.searchAfters = searchAfters;
        }
    }

}

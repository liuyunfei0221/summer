package com.blue.es.common;


import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.model.common.DataAndSearchAfter;

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
@SuppressWarnings({"JavadocDeclaration", "unused"})
public final class EsSearchAfterProcessor {

    /**
     * package search after to query
     *
     * @param builder
     * @param searchAfter
     */
    public static void packageSearchAfter(SearchRequest.Builder builder, String searchAfter) {
        if (isNotNull(builder) && isNotBlank(searchAfter))
            builder.searchAfter(searchAfter);
    }

    /**
     * parse data and search after
     *
     * @param hits
     * @param <T>
     * @return
     */
    public static <T extends Serializable> DataAndSearchAfter<T, String> parseSearchAfter(List<Hit<T>> hits) {
        if (isEmpty(hits))
            return new DataAndSearchAfter<>();

        List<T> data = new ArrayList<>(hits.size());
        SearchAfterHolder searchAfterHolder = new SearchAfterHolder();

        for (Hit<T> hit : hits)
            ofNullable(hit)
                    .ifPresent(h -> {
                        ofNullable(h.source()).ifPresent(data::add);
                        ofNullable(h.sort()).filter(BlueChecker::isNotEmpty)
                                .map(l -> l.get(0)).filter(BlueChecker::isNotBlank).ifPresent(searchAfter -> searchAfterHolder.searchAfter = searchAfter);
                    });

        return new DataAndSearchAfter<>(data, searchAfterHolder.searchAfter);
    }

    /**
     * search after holder
     */
    private static final class SearchAfterHolder {

        /**
         * search after
         */
        private String searchAfter;

        public SearchAfterHolder() {
            this.searchAfter = null;
        }

        public SearchAfterHolder(String searchAfter) {
            this.searchAfter = searchAfter;
        }
    }

}

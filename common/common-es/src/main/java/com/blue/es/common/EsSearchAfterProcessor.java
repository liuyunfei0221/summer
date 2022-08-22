package com.blue.es.common;


import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.blue.basic.common.base.BlueChecker;

import java.util.List;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_DATA;
import static java.util.Optional.ofNullable;

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
    public static <T> String parseSearchAfter(List<Hit<T>> hits) {
        if (isEmpty(hits))
            return EMPTY_DATA.value;

        SearchAfterHolder searchAfterHolder = new SearchAfterHolder();

        for (Hit<T> hit : hits)
            ofNullable(hit)
                    .map(Hit::sort)
                    .filter(BlueChecker::isNotEmpty)
                    .map(l -> l.get(0))
                    .ifPresent(searchAfter -> searchAfterHolder.searchAfter = searchAfter);

        return searchAfterHolder.searchAfter;
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

package com.blue.es.common;


import co.elastic.clients.elasticsearch._types.Time;
import co.elastic.clients.elasticsearch.core.OpenPointInTimeResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.PointInTimeReference;
import com.blue.basic.common.base.BlueChecker;

import static com.blue.basic.common.base.BlueChecker.isNotBlank;
import static com.blue.basic.common.base.BlueChecker.isNotNull;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_DATA;
import static java.util.Optional.ofNullable;

/**
 * es point in time processor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavadocDeclaration", "unused"})
public final class EsPitProcessor {

    /**
     * package pit to query
     *
     * @param builder
     * @param id
     * @param time
     */
    public static void packagePit(SearchRequest.Builder builder, String id, Time time) {
        if (isNotNull(builder) && isNotBlank(id) && isNotNull(time))
            builder.pit(PointInTimeReference.of(b -> b.keepAlive(time).id(id)));
    }

    /**
     * parse pit id
     *
     * @param openPointInTimeResponse
     * @return
     */
    public static String parsePit(OpenPointInTimeResponse openPointInTimeResponse) {
        return ofNullable(openPointInTimeResponse).map(OpenPointInTimeResponse::id).filter(BlueChecker::isNotBlank).orElse(EMPTY_DATA.value);
    }

    /**
     * parse pit id
     *
     * @param shineSearchResponse
     * @return
     */
    public static String parsePit(SearchResponse<?> shineSearchResponse) {
        return ofNullable(shineSearchResponse).map(SearchResponse::pitId).filter(BlueChecker::isNotBlank).orElse(EMPTY_DATA.value);
    }

}

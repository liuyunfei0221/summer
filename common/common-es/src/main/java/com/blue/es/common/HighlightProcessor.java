package com.blue.es.common;

import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.search.Highlight;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.blue.es.model.QueryAndHighlightColumns;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import static com.blue.basic.common.base.BlueChecker.*;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

/**
 * es highlight processor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavadocDeclaration"})
public class HighlightProcessor {

    /**
     * package highlight
     *
     * @param queryAndHighlightColumns
     * @param preTags
     * @param postTags
     */
    public static void packageHighlight(SearchRequest.Builder builder, QueryAndHighlightColumns queryAndHighlightColumns, List<String> preTags, List<String> postTags) {
        List<String> highlightColumns = queryAndHighlightColumns.getHighlightColumns();
        final boolean hasPreTags = isNotEmpty(preTags);
        final boolean hasPostTags = isNotEmpty(postTags);

        if (isNotNull(builder) && isNotEmpty(highlightColumns) && (hasPreTags || hasPostTags))
            builder.highlight(Highlight.of(hb -> {
                for (String column : highlightColumns)
                    if (isNotBlank(column))
                        hb.fields(column, fb -> {
                            if (hasPreTags)
                                fb.preTags(preTags);

                            if (hasPostTags)
                                fb.postTags(postTags);

                            return fb;
                        });

                return hb;
            }));
    }

    /**
     * parse highlight
     *
     * @param hits
     * @param <T>
     * @return
     */
    public static <T> List<T> parseHighlight(List<Hit<T>> hits, Map<String, BiConsumer<List<String>, T>> highlightProcessors) {
        if (isEmpty(hits))
            return emptyList();

        if (isNotEmpty(highlightProcessors)) {
            List<T> sources = new ArrayList<>(hits.size());

            T source;
            Map<String, List<String>> highlight;
            BiConsumer<List<String>, T> processor;

            for (Hit<T> hit : hits) {
                if (isNull(hit))
                    continue;
                
                if (isNotNull(source = hit.source()) && isNotEmpty(highlight = hit.highlight()))
                    for (Map.Entry<String, List<String>> entry : highlight.entrySet())
                        if (isNotNull(processor = highlightProcessors.get(entry.getKey())))
                            processor.accept(entry.getValue(), source);

                sources.add(source);
            }

            return sources;
        }

        return hits.stream().map(Hit::source).collect(toList());
    }

}

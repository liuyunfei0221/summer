package com.blue.es.model;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;

import java.io.Serializable;
import java.util.List;

import static com.blue.basic.common.base.BlueChecker.isNotNull;
import static java.util.Collections.emptyList;

/**
 * query and highlight columns
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class QueryAndHighlightColumns implements Serializable {

    private static final long serialVersionUID = -3593629635967312082L;

    private Query query;

    private List<String> highlightColumns;

    public QueryAndHighlightColumns() {
    }

    public QueryAndHighlightColumns(Query query, List<String> highlightColumns) {
        this.query = query;
        this.highlightColumns = highlightColumns;
    }

    public Query getQuery() {
        return isNotNull(query) ? query : Query.of(builder -> builder.matchAll(MatchAllQuery.of(b -> b.boost(1.0f))));
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public List<String> getHighlightColumns() {
        return isNotNull(highlightColumns) ? highlightColumns : emptyList();
    }

    public void setHighlightColumns(List<String> highlightColumns) {
        this.highlightColumns = highlightColumns;
    }

    @Override
    public String toString() {
        return "QueryAndHighlightColumns{" +
                "query=" + query +
                ", highlightColumns=" + highlightColumns +
                '}';
    }

}

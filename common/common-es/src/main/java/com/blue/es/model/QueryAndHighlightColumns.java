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

    private List<String> columns;

    public QueryAndHighlightColumns() {
    }

    public QueryAndHighlightColumns(Query query, List<String> columns) {
        this.query = query;
        this.columns = columns;
    }

    public Query getQuery() {
        return isNotNull(query) ? query : Query.of(builder -> builder.matchAll(MatchAllQuery.of(b -> b.boost(1.0f))));
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public List<String> getColumns() {
        return isNotNull(columns) ? columns : emptyList();
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    @Override
    public String toString() {
        return "QueryAndHighlightColumns{" +
                "query=" + query +
                ", columns=" + columns +
                '}';
    }

}

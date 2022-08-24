package com.blue.es.model;

import java.io.Serializable;
import java.util.List;

import static com.blue.basic.common.base.BlueChecker.isNotEmpty;
import static java.util.Collections.emptyList;

/**
 * es search after cursor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class SearchAfterCursor implements Serializable {

    private static final long serialVersionUID = 6435873393323727201L;

    /**
     * search after cursor
     */
    private List<String> searchAfter;

    public SearchAfterCursor() {
        this.searchAfter = emptyList();
    }

    public SearchAfterCursor(List<String> searchAfter) {
        this.searchAfter = isNotEmpty(searchAfter) ? searchAfter : emptyList();
    }

    public List<String> getSearchAfter() {
        return searchAfter;
    }

    public void setSearchAfter(List<String> searchAfter) {
        this.searchAfter = searchAfter;
    }

    @Override
    public String toString() {
        return "SearchAfterCursor{" +
                "searchAfter=" + searchAfter +
                '}';
    }
}

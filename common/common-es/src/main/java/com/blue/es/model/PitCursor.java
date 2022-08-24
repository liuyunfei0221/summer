package com.blue.es.model;

import java.io.Serializable;
import java.util.List;

import static com.blue.basic.common.base.BlueChecker.isNotEmpty;
import static com.blue.basic.common.base.BlueChecker.isNotNull;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_DATA;
import static java.util.Collections.emptyList;

/**
 * es pit cursor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class PitCursor implements Serializable {

    private static final long serialVersionUID = -9056648364709931559L;

    /**
     * pit id
     */
    private String id;

    /**
     * search after cursor
     */
    private List<String> searchAfter;

    public PitCursor() {
        this.id = EMPTY_DATA.value;
        this.searchAfter = emptyList();
    }

    public PitCursor(String id, List<String> searchAfter) {
        this.id = isNotNull(id) ? id : EMPTY_DATA.value;
        this.searchAfter = isNotEmpty(searchAfter) ? searchAfter : emptyList();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getSearchAfter() {
        return searchAfter;
    }

    public void setSearchAfter(List<String> searchAfter) {
        this.searchAfter = searchAfter;
    }

    @Override
    public String toString() {
        return "PitCursor{" +
                "id='" + id + '\'' +
                ", searchAfter=" + searchAfter +
                '}';
    }

}

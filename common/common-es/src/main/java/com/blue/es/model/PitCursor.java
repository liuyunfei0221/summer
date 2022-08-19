package com.blue.es.model;

import java.io.Serializable;
import java.util.List;

import static com.blue.basic.common.base.BlueChecker.isNotEmpty;
import static com.blue.basic.common.base.BlueChecker.isNotNull;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_DATA;
import static java.util.Collections.emptyList;

/**
 * es sort processor
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
    private List<String> searchAfters;

    public PitCursor() {
        this.id = EMPTY_DATA.value;
        this.searchAfters = emptyList();
    }

    public PitCursor(String id, List<String> searchAfters) {
        this.id = isNotNull(id) ? id : EMPTY_DATA.value;
        this.searchAfters = isNotEmpty(searchAfters) ? searchAfters : emptyList();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getSearchAfters() {
        return searchAfters;
    }

    public void setSearchAfters(List<String> searchAfters) {
        this.searchAfters = searchAfters;
    }

    @Override
    public String toString() {
        return "PitCursor{" +
                "id='" + id + '\'' +
                ", searchAfters=" + searchAfters +
                '}';
    }

}

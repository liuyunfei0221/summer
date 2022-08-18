package com.blue.es.model;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isNotNull;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_DATA;

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
    private String cursor;

    public PitCursor() {
    }

    public PitCursor(String id, String cursor) {
        this.id = isNotNull(id) ? id : EMPTY_DATA.value;
        this.cursor = isNotNull(cursor) ? cursor : EMPTY_DATA.value;
    }

    public String getId() {
        return isNotNull(id) ? id : EMPTY_DATA.value;
    }

    public void setId(String id) {
        this.id = isNotNull(id) ? id : EMPTY_DATA.value;
    }

    public String getCursor() {
        return isNotNull(cursor) ? cursor : EMPTY_DATA.value;
    }

    public void setCursor(String cursor) {
        this.cursor = isNotNull(cursor) ? cursor : EMPTY_DATA.value;
    }

    @Override
    public String toString() {
        return "PitCursor{" +
                "id='" + id + '\'' +
                ", cursor='" + cursor + '\'' +
                '}';
    }

}

package com.blue.basic.model.event;

import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;
import java.util.List;

import static com.blue.basic.common.base.BlueChecker.isEmpty;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;

/**
 * ids value event
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class IdentitiesEvent implements Serializable {

    private static final long serialVersionUID = -2520090693378458560L;

    /**
     * ids
     */
    private List<Long> ids;

    public IdentitiesEvent() {
    }

    public IdentitiesEvent(List<Long> ids) {
        if (isEmpty(ids))
            throw new BlueException(INVALID_IDENTITY);

        this.ids = ids;
    }

    public List<Long> getIds() {
        return this.ids;
    }

    public void setIds(List<Long> ids) {
        if (isEmpty(ids))
            throw new BlueException(INVALID_IDENTITY);

        this.ids = ids;
    }

    @Override
    public String toString() {
        return "IdentitiesEvent{" +
                "ids=" + ids +
                '}';
    }

}
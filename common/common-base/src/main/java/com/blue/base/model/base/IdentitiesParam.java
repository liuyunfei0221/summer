package com.blue.base.model.base;

import com.blue.base.model.exps.BlueException;

import java.io.Serializable;
import java.util.List;

import static com.blue.base.common.base.BlueChecker.isEmpty;
import static com.blue.base.constant.base.ResponseElement.INVALID_IDENTITY;

/**
 * ids param
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class IdentitiesParam implements Serializable {

    private static final long serialVersionUID = -7710914687734474892L;

    /**
     * id
     */
    private List<Long> ids;

    public IdentitiesParam() {
    }

    public IdentitiesParam(List<Long> ids) {
        this.ids = ids;
    }

    public List<Long> getIds() {
        if (isEmpty(this.ids))
            throw new BlueException(INVALID_IDENTITY);

        return this.ids;
    }

    public void setIds(List<Long> ids) {
        if (isEmpty(ids))
            throw new BlueException(INVALID_IDENTITY);

        this.ids = ids;
    }

    @Override
    public String toString() {
        return "IdentitiesParam{" +
                "ids=" + ids +
                '}';
    }

}

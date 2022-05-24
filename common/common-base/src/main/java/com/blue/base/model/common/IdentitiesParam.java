package com.blue.base.model.common;

import com.blue.base.inter.Asserter;
import com.blue.base.model.exps.BlueException;

import java.io.Serializable;
import java.util.List;

import static com.blue.base.common.base.BlueChecker.isEmpty;
import static com.blue.base.constant.base.ResponseElement.INVALID_IDENTITY;

/**
 * ids param
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class IdentitiesParam implements Serializable, Asserter {

    private static final long serialVersionUID = -7710914687734474892L;

    /**
     * ids
     */
    private List<Long> ids;

    public IdentitiesParam() {
    }

    public IdentitiesParam(List<Long> ids) {
        this.ids = ids;
    }

    @Override
    public void asserts() {
        if (isEmpty(this.ids))
            throw new BlueException(INVALID_IDENTITY);
    }

    public List<Long> getIds() {
        return this.ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    @Override
    public String toString() {
        return "IdentitiesParam{" +
                "ids=" + ids +
                '}';
    }

}

package com.blue.basic.model.common;

import com.blue.basic.inter.Asserter;
import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;
import java.util.List;

import static com.blue.basic.common.base.BlueChecker.isEmpty;
import static com.blue.basic.common.base.ConstantProcessor.assertStatus;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;

/**
 * ids and status param
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class IdentitiesAndStatusParam implements Serializable, Asserter {

    private static final long serialVersionUID = -762253155615381723L;

    /**
     * ids
     */
    private List<Long> ids;

    /**
     * status
     */
    private Integer status;

    public IdentitiesAndStatusParam() {
    }

    public IdentitiesAndStatusParam(List<Long> ids, Integer status) {
        this.ids = ids;
        this.status = status;
    }

    @Override
    public void asserts() {
        if (isEmpty(this.ids))
            throw new BlueException(INVALID_IDENTITY);

        assertStatus(this.status, false);
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "IdentitiesAndStatusParam{" +
                "ids=" + ids +
                ", status=" + status +
                '}';
    }

}

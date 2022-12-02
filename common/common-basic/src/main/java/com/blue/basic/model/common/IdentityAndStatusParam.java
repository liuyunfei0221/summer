package com.blue.basic.model.common;

import com.blue.basic.inter.Asserter;
import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.basic.common.base.ConstantProcessor.assertStatus;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;

/**
 * id and status param
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class IdentityAndStatusParam implements Serializable, Asserter {

    private static final long serialVersionUID = 6596746946993404026L;

    /**
     * id
     */
    private Long id;

    /**
     * status
     */
    private Integer status;

    public IdentityAndStatusParam() {
    }

    public IdentityAndStatusParam(Long id, Integer status) {
        this.id = id;
        this.status = status;
    }

    @Override
    public void asserts() {
        if (isInvalidIdentity(this.id))
            throw new BlueException(INVALID_IDENTITY);

        assertStatus(this.status, false);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "IdentityAndStatusParam{" +
                "id=" + id +
                ", status=" + status +
                '}';
    }

}

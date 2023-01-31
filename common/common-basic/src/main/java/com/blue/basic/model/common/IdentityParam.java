package com.blue.basic.model.common;

import com.blue.basic.inter.Asserter;
import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;

/**
 * id param
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class IdentityParam implements Serializable, Asserter {

    private static final long serialVersionUID = 3631693809321831634L;

    /**
     * id
     */
    private Long id;

    public IdentityParam() {
    }

    public IdentityParam(Long id) {
        this.id = id;
    }

    @Override
    public void asserts() {
        if (isInvalidIdentity(this.id))
            throw new BlueException(INVALID_IDENTITY);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "IdentityParam{" +
                "id=" + id +
                '}';
    }

}
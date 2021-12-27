package com.blue.base.model.base;

import com.blue.base.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.base.common.base.Asserter.isInvalidIdentity;
import static com.blue.base.constant.base.ResponseElement.INVALID_IDENTITY;

/**
 * id param
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class IdentityParam implements Serializable {

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

    public Long getId() {
        if (isInvalidIdentity(this.id))
            throw new BlueException(INVALID_IDENTITY);

        return this.id;
    }

    public void setId(Long id) {
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        this.id = id;
    }

    @Override
    public String toString() {
        return "IdentityDTO{" +
                "id=" + id +
                '}';
    }
}

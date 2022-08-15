package com.blue.basic.model.event;

import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;

/**
 * id value event
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class IdentityEvent implements Serializable {

    private static final long serialVersionUID = 2526678048761933231L;

    /**
     * id
     */
    private Long id;

    public IdentityEvent() {
    }

    public IdentityEvent(Long id) {
        if (isInvalidIdentity(id))
            throw new BlueException(BAD_REQUEST);

        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        if (isInvalidIdentity(id))
            throw new BlueException(BAD_REQUEST);

        this.id = id;
    }

    @Override
    public String toString() {
        return "IdentityEvent{" +
                "id=" + id +
                '}';
    }

}

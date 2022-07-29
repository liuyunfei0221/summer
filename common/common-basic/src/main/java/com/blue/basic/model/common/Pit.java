package com.blue.basic.model.common;

import com.blue.basic.inter.Asserter;
import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isBlank;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;

/**
 * es point in time param
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class Pit implements Serializable, Asserter {

    private static final long serialVersionUID = 6500834746766231018L;

    private String id;

    public Pit() {
    }

    public Pit(String id) {
        this.id = id;
    }

    @Override
    public void asserts() {
        if (isBlank(this.id))
            throw new BlueException(EMPTY_PARAM);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Pit{" +
                "id='" + id + '\'' +
                '}';
    }

}

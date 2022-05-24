package com.blue.base.model.common;

import com.blue.base.inter.Asserter;
import com.blue.base.model.exps.BlueException;

import java.io.Serializable;
import java.util.List;

import static com.blue.base.common.base.BlueChecker.isEmpty;
import static com.blue.base.constant.base.ResponseElement.EMPTY_PARAM;

/**
 * keys param
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "unused"})
public final class ElememtKeysParam implements Serializable, Asserter {

    private static final long serialVersionUID = 2734920052156382753L;

    /**
     * keys
     */
    private List<String> keys;

    public ElememtKeysParam() {
    }

    public ElememtKeysParam(List<String> keys) {
        this.keys = keys;
    }

    @Override
    public void asserts() {
        if (isEmpty(this.keys))
            throw new BlueException(EMPTY_PARAM);
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

    @Override
    public String toString() {
        return "ElememtKeysParam{" +
                "keys=" + keys +
                '}';
    }
}

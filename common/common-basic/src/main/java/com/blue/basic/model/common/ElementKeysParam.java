package com.blue.basic.model.common;

import com.blue.basic.inter.Asserter;
import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;
import java.util.List;

import static com.blue.basic.common.base.BlueChecker.isEmpty;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;

/**
 * keys param
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "unused"})
public final class ElementKeysParam implements Serializable, Asserter {

    private static final long serialVersionUID = 2734920052156382753L;

    /**
     * keys
     */
    private List<String> keys;

    public ElementKeysParam() {
    }

    public ElementKeysParam(List<String> keys) {
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
        return "ElementKeysParam{" +
                "keys=" + keys +
                '}';
    }
}

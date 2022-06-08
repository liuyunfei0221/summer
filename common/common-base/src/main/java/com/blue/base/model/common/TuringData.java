package com.blue.base.model.common;

import com.blue.base.inter.Asserter;
import com.blue.base.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.base.common.base.BlueChecker.isBlank;
import static com.blue.base.constant.common.ResponseElement.NEED_TURING_TEST;

/**
 * turing test data
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "unused"})
public final class TuringData implements Serializable, Asserter {

    private static final long serialVersionUID = -5680590570319900424L;

    private String key;

    private String verify;

    public TuringData() {
    }

    public TuringData(String key, String verify) {
        this.key = key;
        this.verify = verify;
    }

    @Override
    public void asserts() {
        if (isBlank(this.key) || isBlank(this.verify))
            throw new BlueException(NEED_TURING_TEST);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    @Override
    public String toString() {
        return "TuringData{" +
                "key='" + key + '\'' +
                ", verify='" + verify + '\'' +
                '}';
    }

}

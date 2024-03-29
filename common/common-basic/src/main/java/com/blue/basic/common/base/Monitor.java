package com.blue.basic.common.base;

import com.blue.basic.model.exps.BlueException;

import java.util.function.BinaryOperator;
import java.util.function.Predicate;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.constant.common.ResponseElement.*;

/**
 * monitor for value
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces"})
public final class Monitor<T> {

    /**
     * monitored object
     */
    private T monitored;

    /**
     * combiner
     */
    private final BinaryOperator<T> combiner;

    /**
     * asserter
     */
    private final Predicate<T> predicate;

    public Monitor(T monitored, BinaryOperator<T> combiner, Predicate<T> predicate) {
        if (isNull(monitored) || isNull(combiner) || isNull(predicate))
            throw new BlueException(BAD_REQUEST);

        this.monitored = monitored;
        this.combiner = combiner;
        this.predicate = predicate;
    }

    /**
     * get current value
     *
     * @return
     */
    public T getMonitored() {
        return this.monitored;
    }

    /**
     * combine and assert
     *
     * @param data
     * @return
     */
    public boolean operateWithAssert(T data) {
        if (isNull(data))
            throw new BlueException(EMPTY_PARAM);

        synchronized (this) {
            monitored = combiner.apply(monitored, data);
        }

        return predicate.test(monitored);
    }

}
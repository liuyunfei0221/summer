package com.blue.base.common.base;

import com.blue.base.model.exps.BlueException;

import java.util.function.BinaryOperator;
import java.util.function.Predicate;

import static com.blue.base.constant.base.ResponseElement.*;

/**
 * monitor for value
 *
 * @author DarkBlue
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
        if (monitored == null || combiner == null || predicate == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, BAD_REQUEST.message);

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
        if (data == null)
            throw new BlueException(EMPTY_PARAM.status, EMPTY_PARAM.code, EMPTY_PARAM.message);

        synchronized (this) {
            monitored = combiner.apply(monitored, data);
            return predicate.test(monitored);
        }
    }

}

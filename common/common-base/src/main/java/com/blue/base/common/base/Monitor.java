package com.blue.base.common.base;

import java.util.function.BinaryOperator;
import java.util.function.Predicate;

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
        if (monitored == null)
            throw new RuntimeException("monitored can't be null");
        if (combiner == null)
            throw new RuntimeException("combiner can't be null");
        if (predicate == null)
            throw new RuntimeException("predicate can't be null");

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
            throw new RuntimeException("data can't be null");

        synchronized (this) {
            monitored = combiner.apply(monitored, data);
            return predicate.test(monitored);
        }
    }

}

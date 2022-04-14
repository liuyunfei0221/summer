package com.blue.base.common.multitask;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * batch processor inter
 *
 * @param <T>
 * @param <R>
 * @author liuyunfei
 */
public interface BlueProcessor<T, R> {

    /**
     * success process
     * T -> resource ,R -> success result
     *
     * @return processor
     */
    Function<T, R> processor();

    /**
     * fail processor
     * T -> resource ,R -> fail result
     *
     * @return callback
     */
    BiFunction<T, Throwable, R> failProcessor();

}

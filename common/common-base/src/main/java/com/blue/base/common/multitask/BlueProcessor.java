package com.blue.base.common.multitask;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * batch processor inter
 *
 * @param <T>
 * @param <R>
 * @author DarkBlue
 */
public interface BlueProcessor<T, R> {

    /**
     * success process
     * T -> resource ,R -> success result
     *
     * @return 资源处理函数
     */
    Function<T, R> processor();

    /**
     * fail processor
     * T -> resource ,R -> fail result
     *
     * @return 失败处理函数
     */
    BiFunction<T, Throwable, R> failProcessor();

}

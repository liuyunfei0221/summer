package com.blue.base.common.multitask;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 批处理函数约束接口
 *
 * @param <T>
 * @param <R> 用于不论处理结果成功与否的通用结果
 * @author DarkBlue
 */
public interface BlueProcessor<T, R> {

    /**
     * 执行处理
     * T 需要处理的任务资源 R 任务处理成功结果
     *
     * @return 资源处理函数
     */
    Function<T, R> processor();

    /**
     * 失败处理
     * T 需要处理的任务资源 E 异常信息 R 任务处理失败结果
     *
     * @return 失败处理函数
     */
    BiFunction<T, Throwable, R> failProcessor();

}

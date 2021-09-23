package com.blue.base.common.multitask;


import java.util.List;

/**
 * 聚合器接口
 *
 * @param <R>
 * @author DarkBlue
 */
public interface BlueCollector<R> {

    /**
     * 获取聚合结果
     *
     * @return 聚合结果
     */
    List<R> collect();

    /**
     * 任务消费回调
     *
     * @param r 消费结果
     */
    void complete(R r);

}

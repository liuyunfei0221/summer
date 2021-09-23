package com.blue.base.common.base;

import java.util.function.BinaryOperator;
import java.util.function.Predicate;

/**
 * 数值变动监控器
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces"})
public final class Monitor<T> {

    /**
     * 被监控对象
     */
    private T monitored;

    /**
     * 合并器
     */
    private final BinaryOperator<T> combiner;

    /**
     * 断言器
     */
    private final Predicate<T> predicate;

    public Monitor(T monitored, BinaryOperator<T> combiner, Predicate<T> predicate) {
        if (monitored == null)
            throw new RuntimeException("monitored不能为空");
        if (combiner == null)
            throw new RuntimeException("monitored不能为空");
        if (predicate == null)
            throw new RuntimeException("monitored不能为空");

        this.monitored = monitored;
        this.combiner = combiner;
        this.predicate = predicate;
    }

    /**
     * 获取被监控对象当前值
     *
     * @return
     */
    public T getMonitored() {
        return this.monitored;
    }

    /**
     * 操作并断言
     *
     * @param data
     * @return
     */
    public boolean operateWithAssert(T data) {
        if (data == null)
            throw new RuntimeException("data不能为空");

        synchronized (this) {
            monitored = combiner.apply(monitored, data);
            return predicate.test(monitored);
        }
    }

}

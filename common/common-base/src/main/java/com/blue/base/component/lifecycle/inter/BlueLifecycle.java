package com.blue.base.component.lifecycle.inter;

/**
 * 用于处理上下文初始化后bean状态的接口约束,
 * 实现此接口的类会被自动注入容器并会根据优先级顺序在程序启动后执行start,在程序结束时执行stop,
 * 一般用于平滑发布时的优雅上下线处理
 *
 * @author DarkBlue
 * @date 2021/8/13
 * @apiNote
 */
@SuppressWarnings("JavaDoc")
public interface BlueLifecycle {

    /**
     * 启动优先级
     *
     * @return
     */
    int startPrecedence();

    /**
     * 停止优先级
     *
     * @return
     */
    int stopPrecedence();

    /**
     * 启动
     */
    void start();

    /**
     * 停止
     */
    void stop();

}

package com.blue.base.common.multitask;


import java.util.List;

/**
 * collector interface
 *
 * @param <R>
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public interface BlueCollector<R> {

    /**
     * get result of collect
     *
     * @return
     */
    List<R> collect();

    /**
     * complete callback
     *
     * @param r
     */
    void complete(R r);

}

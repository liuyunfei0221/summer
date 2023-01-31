package com.blue.basic.model.common;

import java.io.Serializable;

/**
 * condition count resp
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class ConditionCountResponse<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = -1472078273101939369L;

    private T condition;

    private Long count;

    public ConditionCountResponse() {
    }

    public ConditionCountResponse(T condition, Long count) {
        this.condition = condition;
        this.count = count;
    }

    public T getCondition() {
        return condition;
    }

    public void setCondition(T condition) {
        this.condition = condition;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "ConditionCountResponse{" +
                "condition=" + condition +
                ", count=" + count +
                '}';
    }

}
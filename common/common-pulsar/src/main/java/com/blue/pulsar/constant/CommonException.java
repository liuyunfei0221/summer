package com.blue.pulsar.constant;

/**
 * @author liuyunfei
 * @date 2021/11/3
 * @apiNote
 */
public enum CommonException {

    /**
     * NON_PARAM_EXP
     */
    NON_PARAM_EXP(new RuntimeException("data or params can't be null")),

    /**
     * SEND_FAILED_EXP
     */
    SEND_FAILED_EXP(new RuntimeException("producer send failed"));


    public RuntimeException exp;

    CommonException(RuntimeException exp) {
        this.exp = exp;
    }

}

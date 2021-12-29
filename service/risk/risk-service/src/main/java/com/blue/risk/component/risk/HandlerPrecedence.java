package com.blue.risk.component.risk;

/**
 * @author liuyunfei
 * @date 2021/12/29
 * @apiNote
 */
public enum HandlerPrecedence {

    /**
     * ILLEGAL_REQUEST_VALIDATE
     */
    ILLEGAL_REQUEST_VALIDATE(1);

    public final int precedence;

    HandlerPrecedence(int precedence) {
        this.precedence = precedence;
    }

}

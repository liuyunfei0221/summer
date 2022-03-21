package com.blue.risk.component.risk.constant;

/**
 * @author liuyunfei
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

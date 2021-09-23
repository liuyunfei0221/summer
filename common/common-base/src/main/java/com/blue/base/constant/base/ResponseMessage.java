package com.blue.base.constant.base;

/**
 * 常用响应信息
 *
 * @author liuyunfei
 * @date 2021/9/22
 * @apiNote
 */
public enum ResponseMessage {

    /**
     * 请求内容为空
     */
    EMPTY_REQUEST_BODY("请求内容为空");

    public final String message;

    ResponseMessage(String message) {
        this.message = message;
    }

}

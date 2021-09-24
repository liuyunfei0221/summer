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
    EMPTY_REQUEST_BODY("请求内容为空"),

    /**
     * 请求头数量过多
     */
    TOO_MANY_HEADERS("请求头数量过多"),

    /**
     * 请求头长度过大
     */
    TOO_LARGE_HEADER("请求头长度过大"),

    /**
     * 请求体长度过大
     */
    TOO_LARGE_BODY("请求体长度过大"),

    /**
     * uri过长
     */
    TOO_LARGE_URI("uri过长"),

    /**
     * 加密或解密数据失败
     */
    RSA_FAILED("加解密或签名/验签失败"),

    /**
     * 请求方式非法
     */
    INVALID_REQUEST_METHOD("请求方式非法"),

    /**
     * 主键不能为空或小于1
     */
    INVALID_IDENTITY("主键不能为空或小于1"),

    /**
     * identity不能为空
     */
    INVALID_CONSTANT_IDENTITY("identity不能为空"),

    /**
     * 账号或密码错误
     */
    INVALID_ACCT_OR_PWD("账号或密码错误"),

    /**
     * 文件不存在
     */
    FILE_NOT_EXIST("文件不存在");

    public final String message;

    ResponseMessage(String message) {
        this.message = message;
    }

}

package com.blue.basic.common.content.handler.inter;

/**
 * interface of action for request body process
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@FunctionalInterface
public interface StringHandler {

    /**
     * handleRequestBody
     *
     * @param requestBody
     * @return
     */
    String handle(String requestBody);

}
package com.blue.base.common.content.handler.inter;

/**
 * interface of action for request body process
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
@FunctionalInterface
public interface RequestBodyHandler {

    /**
     * handleRequestBody
     *
     * @param requestBody
     * @return
     */
    String handleRequestBody(String requestBody);

}

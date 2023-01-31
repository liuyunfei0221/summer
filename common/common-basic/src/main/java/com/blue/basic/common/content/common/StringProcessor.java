package com.blue.basic.common.content.common;

import com.blue.basic.common.content.handler.inter.StringHandler;

import java.util.List;

import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;


/**
 * RequestBodyProcessor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
public final class StringProcessor {

    private final List<StringHandler> handlers;

    public StringProcessor(List<StringHandler> handlers) {
        this.handlers = ofNullable(handlers)
                .orElseGet(() -> singletonList(s -> s));
    }

    /**
     * handle
     *
     * @param str
     * @return
     */
    public String handle(String str) {
        String result = str;
        for (StringHandler handler : handlers)
            result = handler.handle(result);

        return result;
    }

}
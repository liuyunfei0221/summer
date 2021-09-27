package com.blue.base.common.content.common;

import com.blue.base.common.content.handler.inter.RequestBodyHandler;
import reactor.util.Logger;

import java.util.List;

import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;


/**
 * RequestBodyProcessor
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
public final class RequestBodyProcessor {

    private static final Logger LOGGER = getLogger(RequestBodyProcessor.class);

    private final List<RequestBodyHandler> processors;

    public RequestBodyProcessor(List<RequestBodyHandler> processors) {
        this.processors = ofNullable(processors)
                .orElse(singletonList(b -> b));
    }

    /**
     * handle
     *
     * @param requestBody
     * @return
     */
    public String handleRequestBody(String requestBody) {
        LOGGER.info("String handleRequestBody(String requestBody), requestBody = {}", requestBody);
        String handledRequestBody = handle(requestBody);
        LOGGER.info("String handleRequestBody(String requestBody), handledRequestBody = {}", handledRequestBody);
        return handledRequestBody;
    }

    /**
     * handle
     *
     * @param requestBody
     * @return
     */
    private String handle(String requestBody) {
        String result = requestBody;
        for (RequestBodyHandler handler : processors)
            result = handler.handleRequestBody(result);

        return result;
    }

}

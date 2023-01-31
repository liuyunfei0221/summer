package com.blue.basic.common.base;

import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.blue.basic.common.base.BlueChecker.isNotNull;
import static com.blue.basic.common.metadata.MetadataProcessor.jsonToMetadata;
import static com.blue.basic.constant.common.BlueHeader.METADATA;
import static java.util.Collections.emptyMap;
import static reactor.core.publisher.Mono.just;

/**
 * metadata getter for reactive
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public class MetadataGetter {

    /**
     * get metadata from request
     *
     * @param serverRequest
     * @return
     */
    public static Map<String, String> getMetadata(ServerRequest serverRequest) {
        return isNotNull(serverRequest) ? jsonToMetadata(serverRequest.headers().firstHeader(METADATA.name)) : emptyMap();
    }

    /**
     * get metadata mono from access
     *
     * @param serverRequest
     * @return
     */
    public static Mono<Map<String, String>> getMetadataReact(ServerRequest serverRequest) {
        return just(getMetadata(serverRequest));
    }

}

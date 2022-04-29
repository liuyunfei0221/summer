package com.blue.base.common.reactive;

import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.blue.base.common.metadata.MetadataProcessor.jsonToMetadata;
import static com.blue.base.constant.base.BlueHeader.METADATA;
import static reactor.core.publisher.Mono.just;

/**
 * metadata getter for reactive
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public class MetadataGetterForReactive {

    /**
     * get metadata from request
     *
     * @param serverRequest
     * @return
     */
    public static Map<String, String> getMetadata(ServerRequest serverRequest) {
        return jsonToMetadata(serverRequest.headers().firstHeader(METADATA.name));
    }

    /**
     * get metadata mono from access
     *
     * @param serverRequest
     * @return
     */
    public static Mono<Map<String, String>> getMetadataReact(ServerRequest serverRequest) {
        return just(jsonToMetadata(serverRequest.headers().firstHeader(METADATA.name)));
    }


}

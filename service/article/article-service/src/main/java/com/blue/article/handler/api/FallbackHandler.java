package com.blue.article.handler.api;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author DarkBlue
 */
@Component
public class FallbackHandler {

    public Mono<ServerResponse> fallback(ServerRequest serverRequest) {
        ServerRequest.Headers headers = serverRequest.headers();
        System.err.println("fallback");
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(Mono.justOrEmpty("FALLBACK"), String.class);
    }

}

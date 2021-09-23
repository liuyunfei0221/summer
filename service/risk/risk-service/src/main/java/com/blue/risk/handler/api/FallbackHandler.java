package com.blue.risk.handler.api;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

@Component
public final class FallbackHandler {

    private static final Logger LOGGER = Loggers.getLogger(FallbackHandler.class);

    public Mono<ServerResponse> fallback(ServerRequest serverRequest) {
        ServerRequest.Headers headers = serverRequest.headers();
        System.err.println("fallback");
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(Mono.justOrEmpty("FALLBACK"), String.class);
    }

}

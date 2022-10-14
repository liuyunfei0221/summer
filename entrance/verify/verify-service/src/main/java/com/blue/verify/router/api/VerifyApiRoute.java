package com.blue.verify.router.api;

import com.blue.verify.handler.api.VerifyApiHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * verify api route
 *
 * @author liuyunfei
 */
@Configuration
public class VerifyApiRoute {

    @Bean
    RouterFunction<ServerResponse> verifyApiRouter(VerifyApiHandler verifyApiHandler) {

        RequestPredicate pathPredicate = path("/blue-verify/verify");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/generate", accept(APPLICATION_JSON), verifyApiHandler::generate)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

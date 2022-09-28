package com.blue.auth.router.api;

import com.blue.auth.handler.api.SessionApiHandler;
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
 * session api route
 *
 * @author liuyunfei
 */
@Configuration
public class SessionApiRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> sessionApiRouter(SessionApiHandler sessionApiHandler) {

        RequestPredicate pathPredicate = path("/blue-auth");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/session", accept(APPLICATION_JSON), sessionApiHandler::insertSession)
                .DELETE("/session", sessionApiHandler::deleteSession)
                .DELETE("/sessions", sessionApiHandler::deleteSessions)
                .PATCH("/session", sessionApiHandler::refreshAccess)
                .PATCH("/secret", accept(APPLICATION_JSON), sessionApiHandler::refreshSecret)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

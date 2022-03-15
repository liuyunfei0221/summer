package com.blue.auth.router.api;

import com.blue.auth.handler.api.AuthApiHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * auth api route
 *
 * @author DarkBlue
 */
@Configuration
public class AuthApiRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> authApiRouter(AuthApiHandler authApiHandler) {

        RequestPredicate pathPredicate = path("/blue-auth/auth");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/login", accept(APPLICATION_JSON), authApiHandler::login)
                .DELETE("/logout", authApiHandler::logout)
                .PUT("/updateAccess", accept(APPLICATION_JSON), authApiHandler::updateAccess)
                .PUT("/resetAccess", accept(APPLICATION_JSON), authApiHandler::resetAccess)
                .PUT("/updateSecret", accept(APPLICATION_JSON), authApiHandler::updateSecret)
                .GET("/authority", authApiHandler::selectAuthority)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

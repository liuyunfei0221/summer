package com.blue.secure.router.api;

import com.blue.secure.handler.api.SecureApiHandler;
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
 * secure api route
 *
 * @author DarkBlue
 */
@Configuration
public class SecureApiRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> secureApiRouter(SecureApiHandler secureApiHandler) {

        RequestPredicate pathPredicate = path("/blue-secure/auth");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/login", accept(APPLICATION_JSON), secureApiHandler::login)
                .DELETE("/logout", secureApiHandler::logout)
                .PUT("/updateSecret", accept(APPLICATION_JSON), secureApiHandler::updateSecret)
                .GET("/authority", secureApiHandler::selectAuthority)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

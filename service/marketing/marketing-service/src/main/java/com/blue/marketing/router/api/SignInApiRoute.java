package com.blue.marketing.router.api;

import com.blue.marketing.handler.api.SignInApiHandler;
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
 * sign in api routers
 *
 * @author DarkBlue
 */
@Configuration
public class SignInApiRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> signInApiRouter(SignInApiHandler signInApiHandler) {

        RequestPredicate pathPredicate = path("/blue-marketing/signIn");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("", accept(APPLICATION_JSON), signInApiHandler::signIn)
                .GET("", signInApiHandler::getSignInRecord)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

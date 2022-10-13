package com.blue.auth.router.api;

import com.blue.auth.handler.api.SecurityQuestionApiHandler;
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
 * security question api route
 *
 * @author liuyunfei
 */
@Configuration
public class SecurityQuestionApiRoute {

    @Bean
    RouterFunction<ServerResponse> securityQuestionApiRouter(SecurityQuestionApiHandler securityQuestionApiHandler) {

        RequestPredicate pathPredicate = path("/blue-auth");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/question", accept(APPLICATION_JSON), securityQuestionApiHandler::insertSecurityQuestion)
                .POST("/questions", accept(APPLICATION_JSON), securityQuestionApiHandler::insertSecurityQuestions)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

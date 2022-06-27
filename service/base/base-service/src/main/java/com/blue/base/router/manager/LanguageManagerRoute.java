package com.blue.base.router.manager;

import com.blue.base.handler.manager.LanguageManagerHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * language api routes
 *
 * @author liuyunfei
 */
@Configuration
public class LanguageManagerRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> languageManagerRouter(LanguageManagerHandler languageManagerHandler) {

        RequestPredicate pathPredicate = path("/blue-base/manager");

        RouterFunction<ServerResponse> routerFunction = route()
                .PUT("/languages", languageManagerHandler::refresh)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

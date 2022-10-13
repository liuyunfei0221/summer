package com.blue.base.router.api;

import com.blue.base.handler.api.LanguageApiHandler;
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
public class LanguageApiRoute {

    @Bean
    RouterFunction<ServerResponse> languageApiRouter(LanguageApiHandler languageApiHandler) {

        RequestPredicate pathPredicate = path("/blue-base");

        RouterFunction<ServerResponse> routerFunction = route()
                .GET("/languages", languageApiHandler::select)
                .GET("/language", languageApiHandler::getDefault)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

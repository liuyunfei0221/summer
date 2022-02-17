package com.blue.analyze.router.api;

import com.blue.analyze.handler.api.AnalyzeApiHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * analyze api routers
 *
 * @author DarkBlue
 */
@Configuration
public class AnalyzeApiRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> dataRouter(AnalyzeApiHandler analyzeApiHandler) {

        RequestPredicate pathPredicate = path("/blue-analyze/analyze");

        RouterFunction<ServerResponse> routerFunction = route()
                .GET("", analyzeApiHandler::getData)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

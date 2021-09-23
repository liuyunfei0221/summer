package com.blue.data.router.api;

import com.blue.data.handler.api.DataApiHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * 用户路由
 *
 * @author DarkBlue
 */
@Configuration
public class DataApiRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> dataRouter(DataApiHandler dataApiHandler) {

        RequestPredicate pathPredicate = path("/blue-data/data");

        RouterFunction<ServerResponse> routerFunction = route()
                .GET("", dataApiHandler::getData)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

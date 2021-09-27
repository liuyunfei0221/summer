package com.blue.base.router.api;

import com.blue.base.handler.api.DictApiHandler;
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
public class BaseApiRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> dictRouter(DictApiHandler dictApiHandler) {

        RequestPredicate pathPredicate = path("/blue-base/dictType");

        RouterFunction<ServerResponse> routerFunction = route()
                .GET("", dictApiHandler::selectDictType)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

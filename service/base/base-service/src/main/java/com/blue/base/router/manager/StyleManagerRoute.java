package com.blue.base.router.manager;

import com.blue.base.handler.manager.StyleManagerHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.blue.base.constant.base.PathVariable.ID;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * style api route
 *
 * @author liuyunfei
 */
@SuppressWarnings("DuplicatedCode")
@Configuration
public class StyleManagerRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> roleManagerRouter(StyleManagerHandler styleManagerHandler) {

        RequestPredicate pathPredicate = path("/blue-base/manager");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/style", accept(APPLICATION_JSON), styleManagerHandler::insert)
                .PUT("/style", accept(APPLICATION_JSON), styleManagerHandler::update)
                .DELETE("/style/{" + ID.key + "}", styleManagerHandler::delete)
                .PUT("/style/active", styleManagerHandler::updateActive)
                .POST("/styles", accept(APPLICATION_JSON), styleManagerHandler::select)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

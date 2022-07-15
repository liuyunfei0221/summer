package com.blue.base.router.manager;

import com.blue.base.handler.manager.AreaManagerHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.blue.basic.constant.common.PathVariable.ID;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * area manager route
 *
 * @author liuyunfei
 */
@SuppressWarnings("DuplicatedCode")
@Configuration
public class AreaManagerRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> areaManagerRouter(AreaManagerHandler areaManagerHandler) {

        RequestPredicate pathPredicate = path("/blue-base/manager");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/area", accept(APPLICATION_JSON), areaManagerHandler::insert)
                .PUT("/area", accept(APPLICATION_JSON), areaManagerHandler::update)
                .DELETE("/area/{" + ID.key + "}", areaManagerHandler::delete)
                .POST("/areas", accept(APPLICATION_JSON), areaManagerHandler::select)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

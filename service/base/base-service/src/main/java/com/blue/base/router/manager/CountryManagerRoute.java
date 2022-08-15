package com.blue.base.router.manager;

import com.blue.base.handler.manager.CountryManagerHandler;
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
 * country manager route
 *
 * @author liuyunfei
 */
@SuppressWarnings("DuplicatedCode")
@Configuration
public class CountryManagerRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> countryManagerRouter(CountryManagerHandler countryManagerHandler) {

        RequestPredicate pathPredicate = path("/blue-base/manager");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/country", accept(APPLICATION_JSON), countryManagerHandler::insert)
                .PUT("/country", accept(APPLICATION_JSON), countryManagerHandler::update)
                .DELETE("/country/{" + ID.key + "}", countryManagerHandler::delete)
                .POST("/countries", accept(APPLICATION_JSON), countryManagerHandler::page)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

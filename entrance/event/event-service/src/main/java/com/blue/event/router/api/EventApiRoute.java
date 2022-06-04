package com.blue.event.router.api;

import com.blue.event.handler.api.EventApiHandler;
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
 * event api route
 *
 * @author liuyunfei
 */
@Configuration
public class EventApiRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> eventApiRouter(EventApiHandler eventApiHandler) {

        RequestPredicate pathPredicate = path("/blue-event");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/report", accept(APPLICATION_JSON), eventApiHandler::report)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

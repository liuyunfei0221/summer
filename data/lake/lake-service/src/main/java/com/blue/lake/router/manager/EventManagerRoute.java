package com.blue.lake.router.manager;

import com.blue.lake.handler.manager.EventManagerHandler;
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
 * @author liuyunfei
 */
@Configuration
public class EventManagerRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> eventRouter(EventManagerHandler eventManagerHandler) {

        RequestPredicate pathPredicate = path("/blue-lake");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/events", accept(APPLICATION_JSON), eventManagerHandler::scroll)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

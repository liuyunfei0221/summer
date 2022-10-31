package com.blue.marketing.router.api;

import com.blue.marketing.handler.api.EventRecordApiHandler;
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
 * attachment api route
 *
 * @author liuyunfei
 */
@Configuration
public class EventRecordApiRoute {

    @Bean
    RouterFunction<ServerResponse> eventRecordApiRouter(EventRecordApiHandler eventRecordApiHandler) {

        RequestPredicate pathPredicate = path("/blue-marketing");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/eventRecords", accept(APPLICATION_JSON), eventRecordApiHandler::scroll)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

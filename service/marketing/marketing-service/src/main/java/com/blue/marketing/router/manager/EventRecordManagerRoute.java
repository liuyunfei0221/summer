package com.blue.marketing.router.manager;

import com.blue.marketing.handler.manager.EventRecordManagerHandler;
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
 * attachment manager route
 *
 * @author liuyunfei
 */
@Configuration
public class EventRecordManagerRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> eventRecordManagerRouter(EventRecordManagerHandler eventRecordManagerHandler) {

        RequestPredicate pathPredicate = path("/blue-marketing/manager");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/eventRecords", accept(APPLICATION_JSON), eventRecordManagerHandler::listEventRecord)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

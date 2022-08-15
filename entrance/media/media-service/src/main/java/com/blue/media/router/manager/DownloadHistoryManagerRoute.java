package com.blue.media.router.manager;

import com.blue.media.handler.manager.DownloadHistoryManagerHandler;
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
 * download history manager route
 *
 * @author liuyunfei
 */
@Configuration
public class DownloadHistoryManagerRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> downloadHistoryManagerRouter(DownloadHistoryManagerHandler downloadHistoryManagerHandler) {

        RequestPredicate pathPredicate = path("/blue-media/manager");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/downloadHistories", accept(APPLICATION_JSON), downloadHistoryManagerHandler::page)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

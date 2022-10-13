package com.blue.media.router.api;

import com.blue.media.handler.api.DownloadHistoryApiHandler;
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
 * download history api route
 *
 * @author liuyunfei
 */
@Configuration
public class DownloadHistoryApiRoute {

    @Bean
    RouterFunction<ServerResponse> downloadHistoryApiRouter(DownloadHistoryApiHandler downloadHistoryApiHandler) {

        RequestPredicate pathPredicate = path("/blue-media");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/downloadHistories/scroll", accept(APPLICATION_JSON), downloadHistoryApiHandler::scroll)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

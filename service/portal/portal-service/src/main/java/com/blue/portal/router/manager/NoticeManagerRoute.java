package com.blue.portal.router.manager;

import com.blue.portal.handler.manager.NoticeManagerHandler;
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
 * notice manager route
 *
 * @author liuyunfei
 */
@Configuration
public class NoticeManagerRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> noticeManagerRouter(NoticeManagerHandler noticeManagerHandler) {

        RequestPredicate pathPredicate = path("/blue-portal/manager");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/notice", accept(APPLICATION_JSON), noticeManagerHandler::insert)
                .PUT("/notice", accept(APPLICATION_JSON), noticeManagerHandler::update)
                .DELETE("/notice/{" + ID.key + "}", noticeManagerHandler::delete)
                .POST("/notices", accept(APPLICATION_JSON), noticeManagerHandler::page)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

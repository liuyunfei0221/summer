package com.blue.portal.router.api;

import com.blue.portal.handler.api.NoticeApiHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.blue.basic.constant.common.PathVariable.TYPE;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * notice api routers
 *
 * @author liuyunfei
 */
@Configuration
public class NoticeApiRoute {

    @Bean
    RouterFunction<ServerResponse> noticeApiRouter(NoticeApiHandler noticeApiHandler) {

        RequestPredicate pathPredicate = path("/blue-portal");

        RouterFunction<ServerResponse> routerFunction = route()
                .GET("/notice/{" + TYPE.key + "}", noticeApiHandler::get)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

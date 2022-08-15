package com.blue.member.router.manager;

import com.blue.member.handler.manager.MemberDetailManagerHandler;
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
 * member detail manager router
 *
 * @author liuyunfei
 */
@Configuration
public class MemberDetailManagerRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> memberDetailManagerRouter(MemberDetailManagerHandler memberDetailManagerHandler) {

        RequestPredicate pathPredicate = path("/blue-member/manager");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/details", accept(APPLICATION_JSON), memberDetailManagerHandler::page)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

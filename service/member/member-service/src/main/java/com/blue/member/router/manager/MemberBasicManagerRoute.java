package com.blue.member.router.manager;

import com.blue.member.handler.manager.MemberBasicManagerHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * member basic manager router
 *
 * @author liuyunfei
 */
@Configuration
public class MemberBasicManagerRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> memberBasicManagerRouter(MemberBasicManagerHandler memberBasicManagerHandler) {

        RequestPredicate pathPredicate = path("/blue-member/manager");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/basics", accept(APPLICATION_JSON), memberBasicManagerHandler::page)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

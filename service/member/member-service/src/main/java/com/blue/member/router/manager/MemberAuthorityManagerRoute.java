package com.blue.member.router.manager;

import com.blue.member.handler.manager.MemberAuthorityManagerHandler;
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
 * member manager router
 *
 * @author liuyunfei
 */
@Configuration
public class MemberAuthorityManagerRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> memberAuthorityManagerRouter(MemberAuthorityManagerHandler memberAuthorityManagerHandler) {

        RequestPredicate pathPredicate = path("/blue-member/manager/authority");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/list", accept(APPLICATION_JSON), memberAuthorityManagerHandler::selectAuthority)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

package com.blue.member.router.api;

import com.blue.member.handler.api.MemberApiHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * member api router
 *
 * @author liuyunfei
 */
@Configuration
public class MemberApiRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> memberApiRouter(MemberApiHandler memberApiHandler) {

        RequestPredicate pathPredicate = path("/blue-member/member");

        RouterFunction<ServerResponse> routerFunction = route()
                .GET("", memberApiHandler::selectMemberInfo)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

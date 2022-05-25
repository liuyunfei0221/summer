package com.blue.member.router.api;

import com.blue.member.handler.api.MemberBasicApiHandler;
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
public class MemberBasicApiRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> memberBasicApiRouter(MemberBasicApiHandler memberBasicApiHandler) {

        RequestPredicate pathPredicate = path("/blue-member/basic");

        RouterFunction<ServerResponse> routerFunction = route()
                .GET("", memberBasicApiHandler::getMemberBasicInfo)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

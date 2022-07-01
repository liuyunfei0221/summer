package com.blue.member.router.api;

import com.blue.member.handler.api.MemberDetailApiHandler;
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
 * member detail api router
 *
 * @author liuyunfei
 */
@Configuration
public class MemberDetailApiRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> memberDetailApiRouter(MemberDetailApiHandler memberDetailApiHandler) {

        RequestPredicate pathPredicate = path("/blue-member/detail");

        RouterFunction<ServerResponse> routerFunction = route()
                .GET("", memberDetailApiHandler::get)
                .PUT("", accept(APPLICATION_JSON), memberDetailApiHandler::update)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

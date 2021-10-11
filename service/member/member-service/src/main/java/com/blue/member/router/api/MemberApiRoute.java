package com.blue.member.router.api;

import com.blue.member.handler.api.MemberApiHandler;
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
 * member api router
 *
 * @author DarkBlue
 */
@Configuration
public class MemberApiRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> memberApiRouter(MemberApiHandler memberApiHandler) {

        RequestPredicate pathPredicate = path("/blue-member/member");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/registry", accept(APPLICATION_JSON), memberApiHandler::registry)
                .GET("", memberApiHandler::getMemberInfo)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

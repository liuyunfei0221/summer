package com.blue.member.router.api;

import com.blue.member.handler.api.MemberRegistryApiHandler;
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
 * member registry router
 *
 * @author DarkBlue
 */
@Configuration
public class MemberRegistryApiRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> memberRegistryApiRouter(MemberRegistryApiHandler memberRegistryApiHandler) {

        RequestPredicate pathPredicate = path("/blue-member/registry");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("", accept(APPLICATION_JSON), memberRegistryApiHandler::registry)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

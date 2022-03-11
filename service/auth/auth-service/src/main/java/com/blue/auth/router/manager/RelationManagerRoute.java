package com.blue.auth.router.manager;

import com.blue.auth.handler.manager.RelationManagerHandler;
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
 * relation manager route
 *
 * @author liuyunfei
 * @date 2021/8/31
 * @apiNote
 */
@SuppressWarnings("DuplicatedCode")
@Configuration
public class RelationManagerRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> relationManagerRouter(RelationManagerHandler relationManagerHandler) {

        RequestPredicate pathPredicate = path("/blue-auth/manager/relation");

        RouterFunction<ServerResponse> routerFunction = route()
                .PUT("/role-res", accept(APPLICATION_JSON), relationManagerHandler::updateAuthorityByRole)
                .PUT("/mem-role", accept(APPLICATION_JSON), relationManagerHandler::updateAuthorityByMember)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

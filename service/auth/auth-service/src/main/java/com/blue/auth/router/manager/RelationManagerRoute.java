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
 * relation manager router
 *
 * @author liuyunfei
 */
@Configuration
public class RelationManagerRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> relationManagerRouter(RelationManagerHandler relationManagerHandler) {

        RequestPredicate pathPredicate = path("/blue-auth/manager/relation");

        RouterFunction<ServerResponse> routerFunction = route()
                .PUT("/role-res", accept(APPLICATION_JSON), relationManagerHandler::updateAuthorityByRole)
                .PUT("/mem-role-insert", accept(APPLICATION_JSON), relationManagerHandler::insertAuthorityByMember)
                .PUT("/mem-role-update", accept(APPLICATION_JSON), relationManagerHandler::updateAuthoritiesByMember)
                .PUT("/mem-role-delete", accept(APPLICATION_JSON), relationManagerHandler::deleteAuthorityByMember)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

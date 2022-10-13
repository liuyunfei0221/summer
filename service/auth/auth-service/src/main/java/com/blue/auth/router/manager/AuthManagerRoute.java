package com.blue.auth.router.manager;

import com.blue.auth.handler.manager.AuthManagerHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.blue.basic.constant.common.PathVariable.MID;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * auth manager route
 *
 * @author liuyunfei
 */
@Configuration
public class AuthManagerRoute {

    @Bean
    RouterFunction<ServerResponse> authManagerRouter(AuthManagerHandler authManagerHandler) {

        RequestPredicate pathPredicate = path("/blue-auth/manager");

        RouterFunction<ServerResponse> routerFunction = route()
                .DELETE("/auth/{" + MID.key + "}", accept(APPLICATION_JSON), authManagerHandler::invalidateAuthByMember)
                .GET("/auth/security/{" + MID.key + "}", accept(APPLICATION_JSON), authManagerHandler::selectSecurityInfos)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

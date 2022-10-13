package com.blue.member.router.api;

import com.blue.member.handler.api.RealNameApiHandler;
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
 * real name api router
 *
 * @author liuyunfei
 */
@Configuration
public class RealNameApiRoute {

    @Bean
    RouterFunction<ServerResponse> realNameApiRouter(RealNameApiHandler realNameApiHandler) {

        RequestPredicate pathPredicate = path("/blue-member/realname");

        RouterFunction<ServerResponse> routerFunction = route()
                .GET("", realNameApiHandler::get)
                .PUT("", accept(APPLICATION_JSON), realNameApiHandler::update)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

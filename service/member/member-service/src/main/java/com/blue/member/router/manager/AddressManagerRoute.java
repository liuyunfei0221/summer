package com.blue.member.router.manager;

import com.blue.member.handler.manager.AddressManagerHandler;
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
 * address manager router
 *
 * @author liuyunfei
 */
@Configuration
public class AddressManagerRoute {

    @Bean
    RouterFunction<ServerResponse> addressManagerRouter(AddressManagerHandler addressManagerHandler) {

        RequestPredicate pathPredicate = path("/blue-member/manager");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/addresses", accept(APPLICATION_JSON), addressManagerHandler::page)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

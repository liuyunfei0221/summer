package com.blue.member.router.api;

import com.blue.member.handler.api.AddressApiHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.blue.basic.constant.common.PathVariable.ID;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * address api router
 *
 * @author liuyunfei
 */
@Configuration
public class AddressApiRoute {

    @Bean
    RouterFunction<ServerResponse> addressApiRouter(AddressApiHandler addressApiHandler) {

        RequestPredicate pathPredicate = path("/blue-member/address");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("", accept(APPLICATION_JSON), addressApiHandler::insert)
                .PUT("", accept(APPLICATION_JSON), addressApiHandler::update)
                .DELETE("/{" + ID.key + "}", addressApiHandler::delete)
                .GET("", addressApiHandler::select)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

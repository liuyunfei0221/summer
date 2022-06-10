package com.blue.member.router.api;

import com.blue.member.handler.api.CardApiHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.blue.base.constant.common.PathVariable.ID;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * card api router
 *
 * @author liuyunfei
 */
@Configuration
public class CardApiRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> cardApiRouter(CardApiHandler cardApiHandler) {

        RequestPredicate pathPredicate = path("/blue-member/card");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("", accept(APPLICATION_JSON), cardApiHandler::insert)
                .PUT("", accept(APPLICATION_JSON), cardApiHandler::update)
                .DELETE("/{" + ID.key + "}", cardApiHandler::delete)
                .GET("", cardApiHandler::select)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

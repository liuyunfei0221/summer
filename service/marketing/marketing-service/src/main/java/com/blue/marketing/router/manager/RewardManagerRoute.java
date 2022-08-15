package com.blue.marketing.router.manager;

import com.blue.marketing.handler.manager.RewardManagerHandler;
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
 * reward manager route
 *
 * @author liuyunfei
 */
@Configuration
public class RewardManagerRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> rewardManagerRouter(RewardManagerHandler rewardManagerHandler) {

        RequestPredicate pathPredicate = path("/blue-marketing/manager");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/reward", accept(APPLICATION_JSON), rewardManagerHandler::insert)
                .PUT("/reward", accept(APPLICATION_JSON), rewardManagerHandler::update)
                .DELETE("/reward/{" + ID.key + "}", rewardManagerHandler::delete)
                .POST("/rewards", accept(APPLICATION_JSON), rewardManagerHandler::page)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

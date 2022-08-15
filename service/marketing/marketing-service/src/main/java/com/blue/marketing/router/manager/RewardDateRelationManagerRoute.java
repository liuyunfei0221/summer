package com.blue.marketing.router.manager;

import com.blue.marketing.handler.manager.RewardDateRelationManagerHandler;
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
 * reward date relation manager route
 *
 * @author liuyunfei
 */
@Configuration
public class RewardDateRelationManagerRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> rewardDateRelationManagerRouter(RewardDateRelationManagerHandler rewardDateRelationManagerHandler) {

        RequestPredicate pathPredicate = path("/blue-marketing/manager");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/relation", accept(APPLICATION_JSON), rewardDateRelationManagerHandler::insert)
                .POST("/date/relation", accept(APPLICATION_JSON), rewardDateRelationManagerHandler::insertByDate)
                .PUT("/relation", accept(APPLICATION_JSON), rewardDateRelationManagerHandler::update)
                .DELETE("/relation/{" + ID.key + "}", rewardDateRelationManagerHandler::delete)
                .POST("/relations", accept(APPLICATION_JSON), rewardDateRelationManagerHandler::page)
                .POST("/date/relations", accept(APPLICATION_JSON), rewardDateRelationManagerHandler::selectByDate)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

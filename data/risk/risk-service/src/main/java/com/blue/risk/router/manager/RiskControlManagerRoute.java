package com.blue.risk.router.manager;

import com.blue.risk.handler.manager.RiskControlManagerHandler;
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
 * risk control manager route
 *
 * @author liuyunfei
 */
@Configuration
public class RiskControlManagerRoute {

    @Bean
    RouterFunction<ServerResponse> riskControlManagerRouter(RiskControlManagerHandler riskControlManagerHandler) {

        RequestPredicate pathPredicate = path("/blue-risk/manager");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/illegalMark", accept(APPLICATION_JSON), riskControlManagerHandler::illegalMark)
                .DELETE("/invalidateAuth", accept(APPLICATION_JSON), riskControlManagerHandler::invalidateAuth)
                .DELETE("/invalidateAuthBatch", riskControlManagerHandler::invalidateAuthBatch)
                .PATCH("/updateMemberBasicStatus", accept(APPLICATION_JSON), riskControlManagerHandler::updateMemberBasicStatus)
                .PATCH("/updateMemberBasicStatusBatch", accept(APPLICATION_JSON), riskControlManagerHandler::updateMemberBasicStatusBatch)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

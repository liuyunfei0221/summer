package com.blue.agreement.router.manager;

import com.blue.agreement.handler.manager.AgreementManagerHandler;
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
 * agreement manager route
 *
 * @author liuyunfei
 */
@Configuration
public class AgreementManagerRoute {

    @Bean
    RouterFunction<ServerResponse> agreementManagerRouter(AgreementManagerHandler agreementManagerHandler) {

        RequestPredicate pathPredicate = path("/blue-agreement/manager");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/agreement", accept(APPLICATION_JSON), agreementManagerHandler::insert)
                .POST("/agreements", accept(APPLICATION_JSON), agreementManagerHandler::page)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

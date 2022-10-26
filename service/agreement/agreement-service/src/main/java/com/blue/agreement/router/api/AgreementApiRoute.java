package com.blue.agreement.router.api;

import com.blue.agreement.handler.api.AgreementApiHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.blue.basic.constant.common.PathVariable.TYPE;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * agreement api routers
 *
 * @author liuyunfei
 */
@Configuration
public class AgreementApiRoute {

    @Bean
    RouterFunction<ServerResponse> agreementApiRouter(AgreementApiHandler agreementApiHandler) {

        RequestPredicate pathPredicate = path("/blue-agreement");

        RouterFunction<ServerResponse> routerFunction = route()
                .GET("/agreement/{" + TYPE.key + "}", agreementApiHandler::get)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

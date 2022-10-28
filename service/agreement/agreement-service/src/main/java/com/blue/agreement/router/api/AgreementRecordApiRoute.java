package com.blue.agreement.router.api;

import com.blue.agreement.handler.api.AgreementRecordApiHandler;
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
 * agreement record api routers
 *
 * @author liuyunfei
 */
@Configuration
public class AgreementRecordApiRoute {

    @Bean
    RouterFunction<ServerResponse> agreementRecordApiRouter(AgreementRecordApiHandler agreementRecordApiHandler) {

        RequestPredicate pathPredicate = path("/blue-agreement/agreementRecord");

        RouterFunction<ServerResponse> routerFunction = route()
                .GET("/unsigned", agreementRecordApiHandler::selectUnsigned)
                .POST("", accept(APPLICATION_JSON), agreementRecordApiHandler::sign)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}
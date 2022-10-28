package com.blue.agreement.router.manager;

import com.blue.agreement.handler.manager.AgreementRecordManagerHandler;
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
 * agreement record manager route
 *
 * @author liuyunfei
 */
@Configuration
public class AgreementRecordManagerRoute {

    @Bean
    RouterFunction<ServerResponse> agreementRecordManagerRouter(AgreementRecordManagerHandler agreementRecordManagerHandler) {

        RequestPredicate pathPredicate = path("/blue-agreement/manager");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/agreementRecords", accept(APPLICATION_JSON), agreementRecordManagerHandler::page)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

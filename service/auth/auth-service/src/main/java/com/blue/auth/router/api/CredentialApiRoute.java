package com.blue.auth.router.api;

import com.blue.auth.handler.api.CredentialApiHandler;
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
 * credential api route
 *
 * @author liuyunfei
 */
@Configuration
public class CredentialApiRoute {

    @Bean
    RouterFunction<ServerResponse> credentialApiRouter(CredentialApiHandler credentialApiHandler) {

        RequestPredicate pathPredicate = path("/blue-auth");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/credential", accept(APPLICATION_JSON), credentialApiHandler::insertCredential)
                .PUT("/credential", accept(APPLICATION_JSON), credentialApiHandler::updateCredential)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

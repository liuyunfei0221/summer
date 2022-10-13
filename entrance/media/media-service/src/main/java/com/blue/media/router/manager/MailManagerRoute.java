package com.blue.media.router.manager;

import com.blue.media.handler.manager.MailManagerHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * @author liuyunfei
 */
@Configuration
public class MailManagerRoute {

    @Bean
    RouterFunction<ServerResponse> mailManagerRouter(MailManagerHandler mailManagerHandler) {

        RequestPredicate pathPredicate = path("/blue-media/mail");

        RouterFunction<ServerResponse> routerFunction = route()
                .GET("/send", accept(APPLICATION_JSON), mailManagerHandler::testSend)
                .GET("/read", accept(APPLICATION_JSON), mailManagerHandler::testRead)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

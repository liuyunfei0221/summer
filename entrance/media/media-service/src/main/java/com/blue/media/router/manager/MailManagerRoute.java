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
 * @date 2022/1/5
 * @apiNote
 */
@Configuration
public class MailManagerRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> mailRouter(MailManagerHandler mailManagerHandler) {

        RequestPredicate pathPredicate = path("/blue-media/mail");

        RouterFunction<ServerResponse> routerFunction = route()
                .GET("/test", accept(APPLICATION_JSON), mailManagerHandler::testMail)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

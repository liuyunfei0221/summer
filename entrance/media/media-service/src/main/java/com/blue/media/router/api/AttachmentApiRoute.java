package com.blue.media.router.api;

import com.blue.media.handler.api.AttachmentApiHandler;
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
 * attachment api route
 *
 * @author liuyunfei
 */
@Configuration
public class AttachmentApiRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> attachmentApiRouter(AttachmentApiHandler attachmentApiHandler) {

        RequestPredicate pathPredicate = path("/blue-media");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/attachments/scroll", accept(APPLICATION_JSON), attachmentApiHandler::scroll)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

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
 * @author DarkBlue
 */
@Configuration
public class AttachmentApiRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> attachmentRouter(AttachmentApiHandler attachmentApiHandler) {

        RequestPredicate pathPredicate = path("/blue-media/attachment");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/list", accept(APPLICATION_JSON), attachmentApiHandler::listAttachment)
                .POST("/withdraw", accept(APPLICATION_JSON), attachmentApiHandler::withdraw)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

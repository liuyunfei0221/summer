package com.blue.media.router.manager;

import com.blue.media.handler.manager.AttachmentManagerHandler;
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
 * attachment manager route
 *
 * @author liuyunfei
 */
@Configuration
public class AttachmentManagerRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> attachmentManagerRouter(AttachmentManagerHandler attachmentManagerHandler) {

        RequestPredicate pathPredicate = path("/blue-media/manager");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/attachments", accept(APPLICATION_JSON), attachmentManagerHandler::listAttachment)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

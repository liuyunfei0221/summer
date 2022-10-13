package com.blue.media.router.manager;

import com.blue.media.handler.manager.MessageTemplateManagerHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.blue.basic.constant.common.PathVariable.ID;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * message template manager route
 *
 * @author liuyunfei
 */
@SuppressWarnings("DuplicatedCode")
@Configuration
public class MessageTemplateManagerRoute {

    @Bean
    RouterFunction<ServerResponse> messageTemplateManagerRouter(MessageTemplateManagerHandler messageTemplateManagerHandler) {

        RequestPredicate pathPredicate = path("/blue-media/manager");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/messageTemplate", accept(APPLICATION_JSON), messageTemplateManagerHandler::insert)
                .PUT("/messageTemplate", accept(APPLICATION_JSON), messageTemplateManagerHandler::update)
                .DELETE("/messageTemplate/{" + ID.key + "}", messageTemplateManagerHandler::delete)
                .POST("/messageTemplates", accept(APPLICATION_JSON), messageTemplateManagerHandler::page)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

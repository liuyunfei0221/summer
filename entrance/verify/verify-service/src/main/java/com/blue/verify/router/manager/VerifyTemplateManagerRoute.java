package com.blue.verify.router.manager;

import com.blue.verify.handler.manager.VerifyTemplateManagerHandler;
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
 * config manager route
 *
 * @author liuyunfei
 */
@SuppressWarnings("DuplicatedCode")
@Configuration
public class VerifyTemplateManagerRoute {

    @Bean
    RouterFunction<ServerResponse> verifyTemplateManagerRouter(VerifyTemplateManagerHandler verifyTemplateManagerHandler) {

        RequestPredicate pathPredicate = path("/blue-verify/manager");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/verifyTemplate", accept(APPLICATION_JSON), verifyTemplateManagerHandler::insert)
                .PUT("/verifyTemplate", accept(APPLICATION_JSON), verifyTemplateManagerHandler::update)
                .DELETE("/verifyTemplate/{" + ID.key + "}", verifyTemplateManagerHandler::delete)
                .POST("/verifyTemplates", accept(APPLICATION_JSON), verifyTemplateManagerHandler::page)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}

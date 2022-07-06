package com.blue.media.router.manager;

import com.blue.media.handler.manager.QrCodeConfigManagerHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.blue.base.constant.common.PathVariable.ID;
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
public class QrCodeConfigManagerRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> qrCodeConfigManagerRouter(QrCodeConfigManagerHandler qrCodeConfigManagerHandler) {

        RequestPredicate pathPredicate = path("/blue-media/manager");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/qrCodeConfig", accept(APPLICATION_JSON), qrCodeConfigManagerHandler::insert)
                .PUT("/qrCodeConfig", accept(APPLICATION_JSON), qrCodeConfigManagerHandler::update)
                .DELETE("/qrCodeConfig/{" + ID.key + "}", qrCodeConfigManagerHandler::delete)
                .POST("/qrCodeConfigs", accept(APPLICATION_JSON), qrCodeConfigManagerHandler::select)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}
